package cn.sinozg.applet.biz.notify.timer;

import cn.sinozg.applet.biz.notify.collect.AbstractCollect;
import cn.sinozg.applet.biz.notify.collect.UnitConvert;
import cn.sinozg.applet.biz.notify.collect.impl.JdbcCommonCollect;
import cn.sinozg.applet.biz.notify.core.AlerterContext;
import cn.sinozg.applet.biz.notify.dispatch.CollectDataDispatch;
import cn.sinozg.applet.biz.notify.model.collect.MetricsField;
import cn.sinozg.applet.biz.notify.model.collect.MetricsInfo;
import cn.sinozg.applet.biz.notify.model.collect.TimerJobInfo;
import cn.sinozg.applet.biz.notify.model.collect.TransformResult;
import cn.sinozg.applet.biz.notify.model.proto.CollectCode;
import cn.sinozg.applet.biz.notify.model.proto.CollectFieldParams;
import cn.sinozg.applet.biz.notify.model.proto.CollectMetricsParams;
import cn.sinozg.applet.biz.notify.model.proto.CollectRowsParams;
import cn.sinozg.applet.biz.notify.util.AlerterUtil;
import cn.sinozg.applet.biz.notify.util.CollectUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import io.netty.util.Timeout;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;


/**
 * @Description
 * @Copyright Copyright (c) 2024
 * @author xieyubin
 * @since 2024-01-22 18:42:28
 */
@Data
@Slf4j
public class MetricsCollect implements Runnable, Comparable<MetricsCollect> {
    /**
     * Scheduling alarm threshold time 100ms
     */
    private static final long WARN_DISPATCH_TIME = 100;
    /**
     * collector identity
     */
    protected String collectorIdentity;
    /**
     * Tenant ID
     */
    protected String tenantId;
    /**
     * Monitor ID
     */
    protected String monitorId;
    /**
     * Monitoring type name
     */
    protected String app;
    /**
     * Metrics configuration
     */
    protected MetricsInfo metrics;
    /**
     * time wheel timeout
     */
    protected Timeout timeout;
    /**
     * Task and Data Scheduling
     */
    protected CollectDataDispatch collectDataDispatch;
    /**
     * task execution priority
     */
    protected byte runPriority;
    /**
     * Periodic collection or one-time collection true-periodic false-one-time
     */
    protected boolean isCyclic;
    /**
     * Time for creating collection task
     */
    protected long newTime;
    /**
     * Start time of the collection task
     */
    protected long startTime;

    protected List<UnitConvert> unitConvertList;

    public MetricsCollect(MetricsInfo metrics, Timeout timeout,
                          CollectDataDispatch collectDataDispatch,
                          String collectorIdentity,
                          List<UnitConvert> unitConvertList) {
        this.newTime = System.currentTimeMillis();
        this.timeout = timeout;
        this.metrics = metrics;
        this.collectorIdentity = collectorIdentity;
        WheelTimerTask timerJob = (WheelTimerTask) timeout.task();
        TimerJobInfo job = timerJob.getJob();
        this.monitorId = job.getMonitorId();
        this.tenantId = job.getTenantId();
        this.app = job.getApp();
        this.collectDataDispatch = collectDataDispatch;
        this.isCyclic = job.isCyclic();
        this.unitConvertList = unitConvertList;
        // Temporary one-time tasks are executed with high priority
        if (isCyclic) {
            runPriority = (byte) -1;
        } else {
            runPriority = (byte) 1;
        }
    }

    @Override
    public void run() {
        this.startTime = System.currentTimeMillis();
        setNewThreadName(monitorId, app, startTime, metrics);
        CollectMetricsParams response = CollectMetricsParams.builder()
                .app(app).id(monitorId).tenantId(tenantId)
                .build();

        response.setMetrics(metrics.getName());
        // According to the metrics collection protocol, application type, etc., 
        // dispatch to the real application metrics collection implementation class
        AbstractCollect abstractCollect = new JdbcCommonCollect();
        try {
            abstractCollect.collect(response, monitorId, app, metrics);
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg == null && e.getCause() != null) {
                msg = e.getCause().getMessage();
            }
            log.error("[Metrics Collect]: {}.", msg, e);
            response.setCode(CollectCode.FAIL);
            if (msg != null) {
                response.setMsg(msg);
            }
        }
        // Alias attribute expression replacement calculation
        if (fastFailed()) {
            return;
        }
        calculateFields(metrics, response);
        CollectMetricsParams metricsData = validateResponse(response);
        collectDataDispatch.dispatchCollectData(timeout, metrics, metricsData);
    }


    /**
     * Calculate the real metrics value according to the calculates and aliasFields configuration
     *
     * @param metrics     Metrics configuration     
     * @param collectData Data collection    
     */
    private void calculateFields(MetricsInfo metrics, CollectMetricsParams collectData) {
        collectData.setPriority(metrics.getPriority());
        List<CollectFieldParams> fieldList = new LinkedList<>();
        for (MetricsField field : metrics.getFields()) {
            CollectFieldParams.CollectFieldParamsBuilder fieldParamsBuilder = CollectFieldParams.builder()
                    .label(field.isLabel()).name(field.getField())
                    .type(field.getType());
            if (field.getUnit() != null) {
                fieldParamsBuilder.unit(field.getUnit());
            }
            fieldList.add(fieldParamsBuilder.build());
        }

        AlerterUtil.setList(collectData, fieldList, CollectMetricsParams::getFields, CollectMetricsParams::setFields);

        List<CollectRowsParams> aliasRowList = collectData.getValue();
        if (aliasRowList == null || aliasRowList.isEmpty()) {
            return;
        }
        collectData.setValue(null);
        // Preprocess calculates first    
        if (metrics.getCalculates() == null) {
            metrics.setCalculates(Collections.emptyList());
        }
        // eg: database_pages=Database pages unconventional mapping 
        Map<String, String> fieldAliasMap = new HashMap<>(8);
        Map<String, Expression> fieldExpressionMap = filterMap(metrics.getCalculates(), c -> transformCal(c, fieldAliasMap), TransformResult::getExpression);
        if (metrics.getUnits() == null) {
            metrics.setUnits(Collections.emptyList());
        }
        Map<String, MutablePair<String, String>> fieldUnitMap = filterMap(metrics.getUnits(), this::transformUnit, TransformResult::getUnit);
        List<MetricsField> fields = metrics.getFields();
        List<String> aliasFields = metrics.getAliasFields();
        Map<String, String> aliasFieldValueMap = new HashMap<>(16);
        Map<String, Object> fieldValueMap = new HashMap<>(16);
        CollectRowsParams rows;
        for (CollectRowsParams aliasRow : aliasRowList) {
            rows = new CollectRowsParams();
            for (int aliasIndex = 0; aliasIndex < aliasFields.size(); aliasIndex++) {
                String aliasFieldValue = aliasRow.getColumns().get(aliasIndex);
                if (!AlerterContext.NULL_VALUE.equals(aliasFieldValue)) {
                    aliasFieldValueMap.put(aliasFields.get(aliasIndex), aliasFieldValue);
                }
            }

            for (MetricsField field : fields) {
                String realField = field.getField();
                Expression expression = fieldExpressionMap.get(realField);
                String value = null;
                String aliasFieldUnit = null;
                if (expression != null) {
                    // If there is a calculation expression, calculate the value
                    if (AlerterContext.TYPE_NUMBER == field.getType()) {
                        for (String variable : expression.getVariableFullNames()) {
                            // extract double value and unit from aliasField value
                            ImmutablePair<Double, String> doubleAndUnit = CollectUtil.extractDoubleAndUnitFromStr(aliasFieldValueMap.get(variable));
                            if (doubleAndUnit != null) {
                                Double doubleValue = doubleAndUnit.getKey();
                                aliasFieldUnit = doubleAndUnit.getValue();
                                fieldValueMap.put(variable, doubleValue);
                            } else {
                                fieldValueMap.put(variable, null);
                            }
                        }
                    } else {
                        for (String variable : expression.getVariableFullNames()) {
                            String strValue = aliasFieldValueMap.get(variable);
                            fieldValueMap.put(variable, strValue);
                        }
                    }
                    try {
                        // valueList为空时也执行,涵盖纯字符串赋值表达式
                        Object objValue = expression.execute(fieldValueMap);
                        if (objValue != null) {
                            value = String.valueOf(objValue);
                        }
                    } catch (Exception e) {
                        log.info("[calculates execute warning] {}.", e.getMessage());
                    }
                } else {
                    // does not exist then map the alias value
                    String aliasField = fieldAliasMap.get(realField);
                    if (aliasField != null) {
                        value = aliasFieldValueMap.get(aliasField);
                    } else {
                        value = aliasFieldValueMap.get(realField);
                    }
                    if (value != null) {
                        final byte fieldType = field.getType();
                        if (fieldType == AlerterContext.TYPE_NUMBER) {
                            ImmutablePair<Double, String> doubleAndUnit = CollectUtil.extractDoubleAndUnitFromStr(value);
                            if (doubleAndUnit != null) {
                                if (doubleAndUnit.getKey() != null) {
                                    value = String.valueOf(doubleAndUnit.getKey());
                                }
                                aliasFieldUnit = doubleAndUnit.getValue();
                            }
                        } else if (fieldType == AlerterContext.TYPE_TIME) {
                            final int tempValue;
                            value = (tempValue = AlerterUtil.parseTimeStrToSecond(value)) == -1 ? null : String.valueOf(tempValue);
                        }
                    }
                }

                MutablePair<String, String> unitPair = fieldUnitMap.get(realField);
                if (aliasFieldUnit != null) {
                    if (unitPair != null) {
                        unitPair.setLeft(aliasFieldUnit);
                    } else if (field.getUnit() != null && !aliasFieldUnit.equalsIgnoreCase(field.getUnit())) {
                        unitPair = MutablePair.of(aliasFieldUnit, field.getUnit());
                    }
                }
                if (value != null && unitPair != null) {
                    for (UnitConvert unitConvert : unitConvertList) {
                        if (unitConvert.checkUnit(unitPair.getLeft()) && unitConvert.checkUnit(unitPair.getRight())) {
                            value = unitConvert.convert(value, unitPair.getLeft(), unitPair.getRight());
                        }
                    }
                }
                // Handle metrics values that may have units such as 34%, 34Mb, and limit values to 4 decimal places
                if (AlerterContext.TYPE_NUMBER == field.getType()) {
                    value = AlerterUtil.parseDoubleStr(value, field.getUnit());
                }
                if (value == null) {
                    value = AlerterContext.NULL_VALUE;
                }
                PojoUtil.setBeanList(rows, value, CollectRowsParams::getColumns, CollectRowsParams::setColumns);
                fieldValueMap.clear();
            }
            aliasFieldValueMap.clear();
            PojoUtil.setBeanList(collectData, rows, CollectMetricsParams::getValue, CollectMetricsParams::setValue);
        }
    }


    /**
     * @param cal cal
     * @param fieldAliasMap field alias map
     * @return expr
     */
    private TransformResult transformCal(String cal, Map<String, String> fieldAliasMap) {
        int splitIndex = cal.indexOf("=");
        String field = cal.substring(0, splitIndex).trim();
        String expressionStr = cal.substring(splitIndex + 1).trim().replace("\\#", "#");
        Expression expression;
        try {
            expression = AviatorEvaluator.compile(expressionStr, true);
        } catch (Exception e) {
            fieldAliasMap.put(field, expressionStr);
            return null;
        }
        return new TransformResult(field, expression);
    }


    /**
     * transform unit
     * @param unit unit
     * @return units
     */
    private TransformResult transformUnit(String unit) {
        int equalIndex = unit.indexOf("=");
        int arrowIndex = unit.indexOf("->");
        if (equalIndex < 0 || arrowIndex < 0) {
            return null;
        }
        String field = unit.substring(0, equalIndex).trim();
        String originUnit = unit.substring(equalIndex + 1, arrowIndex).trim();
        String newUnit = unit.substring(arrowIndex + 2).trim();
        return new TransformResult(field, originUnit, newUnit);
    }

    private boolean fastFailed() {
        return this.timeout == null || this.timeout.isCancelled();
    }

    private CollectMetricsParams validateResponse(CollectMetricsParams builder) {
        long endTime = System.currentTimeMillis();
        builder.setTime(endTime);
        long runningTime = endTime - startTime;
        long allTime = endTime - newTime;
        if (startTime - newTime >= WARN_DISPATCH_TIME) {
            log.warn("[Collector Dispatch Warn, Dispatch Use {}ms.", startTime - newTime);
        }
        if (builder.getCode() != CollectCode.SUCCESS) {
            log.info("[Collect Failed, Run {}ms, All {}ms] Reason: {}", runningTime, allTime, builder.getMsg());
        } else {
            log.info("[Collect Success, Run {}ms, All {}ms].", runningTime, allTime);
        }
        return builder;
    }

    private <T> Map<String, T> filterMap (List<String> list, Function<String, TransformResult> mapper, Function<TransformResult, T> valueMapper){
        return list.stream().map(mapper)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(TransformResult::getField, valueMapper, (o, n) -> n));
    }

    private void setNewThreadName(String monitorId, String app, long startTime, MetricsInfo metrics) {
        String builder = monitorId + "-" + app + "-" + metrics.getName() +
                "-" + String.valueOf(startTime).substring(9);
        Thread.currentThread().setName(builder);
    }

    @Override
    public int compareTo(MetricsCollect collect) {
        return runPriority - collect.runPriority;
    }
}
