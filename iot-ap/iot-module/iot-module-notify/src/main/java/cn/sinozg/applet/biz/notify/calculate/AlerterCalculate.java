package cn.sinozg.applet.biz.notify.calculate;

import cn.sinozg.applet.biz.notify.core.AlerterContext;
import cn.sinozg.applet.biz.notify.core.TagsContext;
import cn.sinozg.applet.biz.notify.model.AlerterDefineInfo;
import cn.sinozg.applet.biz.notify.model.AlerterTagItemInfo;
import cn.sinozg.applet.biz.notify.model.NotifierAlerterInfo;
import cn.sinozg.applet.biz.notify.model.proto.CollectCode;
import cn.sinozg.applet.biz.notify.model.proto.CollectFieldParams;
import cn.sinozg.applet.biz.notify.model.proto.CollectMetricsParams;
import cn.sinozg.applet.biz.notify.model.proto.CollectRowsParams;
import cn.sinozg.applet.biz.notify.reduce.AlerterCommonReduce;
import cn.sinozg.applet.biz.notify.service.AlerterDataQueueService;
import cn.sinozg.applet.biz.notify.service.AlerterDataService;
import cn.sinozg.applet.biz.notify.util.AlerterThread;
import cn.sinozg.applet.biz.notify.util.AlerterUtil;
import cn.sinozg.applet.common.constant.Constants;
import cn.sinozg.applet.common.utils.DateUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.exception.CompileExpressionErrorException;
import com.googlecode.aviator.exception.ExpressionRuntimeException;
import com.googlecode.aviator.exception.ExpressionSyntaxErrorException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 启动一个监控台，消费任务。根据条件过滤
 *
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-20 15:32
 */
@Slf4j
@Component
public class AlerterCalculate {

    private final AlerterCommonReduce alerterCommonReduce;

    private final AlerterDataService alerterDataService;

    private final AlerterDataQueueService dataQueueService;
    /**
     * The alarm in the process is triggered
     * 触发中告警信息
     * key - monitorId+alertDefineId+tags 为普通阈值告警 ｜ The alarm is a common threshold alarm
     * key - monitorId 为任务状态可用性可达性告警 ｜ Indicates the monitoring status availability reachability alarm
     */
    private final Map<String, NotifierAlerterInfo>  triggeredAlerterMap;

    private final Map<String, NotifierAlerterInfo> notRecoveredAlerterMap;

    private static final String SYSTEM_VALUE_ROW_COUNT = "system_value_row_count";

    public AlerterCalculate(AlerterCommonReduce alerterCommonReduce, AlerterDataService alerterDataService, AlerterDataQueueService dataQueueService){
        this.triggeredAlerterMap = new ConcurrentHashMap<>(16);
        this.notRecoveredAlerterMap = new ConcurrentHashMap<>(16);
        this.alerterCommonReduce = alerterCommonReduce;
        this.alerterDataService = alerterDataService;
        this.dataQueueService = dataQueueService;
        NotifierAlerterInfo alerterInfo = new NotifierAlerterInfo();
        alerterInfo.setDataStatus(Constants.STATUS_02);
        String id = "id_aaa";
        String app = "app_jq";
        String name = "监控台";
        Map<String, String> tags = new HashMap<>(16);
        tags.put(TagsContext.TAG_MONITOR_ID, id);
        tags.put(TagsContext.TAG_MONITOR_NAME, app);
        tags.put(TagsContext.TAG_MONITOR_APP, name);
        alerterInfo.setTags(tags);
        alerterInfo.setTarget(AlerterContext.AVAILABILITY);
        this.notRecoveredAlerterMap.put(id + AlerterContext.AVAILABILITY, alerterInfo);
        startCalculate();
    }

    private void startCalculate() {
        Runnable runnable = () -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    CollectMetricsParams metricsData = dataQueueService.pollMetricsDataToAlerter();
                    if (metricsData != null) {
                        calculate(metricsData);
                    }
                } catch (InterruptedException ignored) {
                } catch (Exception e) {
                    log.error("calculate alarm error: {}.", e.getMessage(), e);
                }
            }
        };
        AlerterThread.execute(runnable);
        AlerterThread.execute(runnable);
        AlerterThread.execute(runnable);
    }

    private void calculate(CollectMetricsParams metricsData) {
        LocalDateTime ldt = LocalDateTime.now();
        String monitorId = String.valueOf(metricsData.getId());
        String app = metricsData.getApp();
        if (app.startsWith(AlerterContext.PROMETHEUS_APP_PREFIX)) {
            app = AlerterContext.PROMETHEUS;
        }
        String metrics = metricsData.getMetrics();
        // If the metrics whose scheduling priority is 0 has the status of collecting response data UN_REACHABLE/UN_CONNECTABLE,
        // the highest severity alarm is generated to monitor the status change
        if (metricsData.getPriority() == 0) {
            handlerAvailableMetrics(monitorId, app, metricsData);
        }
        // Query the alarm definitions associated with the metrics of the monitoring type
        // field - define[]
        Map<String, List<AlerterDefineInfo>> defineMap = alerterDataService.monitorBindAlerterDefines(monitorId, app, metrics);
        if (defineMap.isEmpty()) {
            return;
        }
        List<CollectFieldParams> fields = metricsData.getFields();
        Map<String, Object> fieldValueMap = new HashMap<>(16);
        int valueRowCount = metricsData.getValueCount();
        for (Map.Entry<String, List<AlerterDefineInfo>> entry : defineMap.entrySet()) {
            List<AlerterDefineInfo> defines = entry.getValue();
            for (AlerterDefineInfo define : defines) {
                final String expr = define.getExpr();
                if (StringUtils.isBlank(expr)) {
                    continue;
                }
                if (expr.contains(SYSTEM_VALUE_ROW_COUNT) && metricsData.getValueCount() == 0) {
                    fieldValueMap.put(SYSTEM_VALUE_ROW_COUNT, valueRowCount);
                    try {
                        boolean match = execAlertExpression(fieldValueMap, expr);
                        try {
                            if (match) {
                                // If the threshold rule matches, the number of times the threshold has been triggered is determined and an alarm is triggered
                                // 阈值规则匹配，判断已触发阈值次数，触发告警
                                afterThresholdRuleMatch(ldt, monitorId, app, metrics, StringUtils.EMPTY, fieldValueMap, define);
                                // 若此阈值已被触发，则其它数据行的触发忽略
                                continue;
                            } else {
                                String alarmKey = monitorId + define.getId();
                                triggeredAlerterMap.remove(alarmKey);
                                if (define.isRecoverNotice()) {
                                    handleRecoveredAlert(ldt, define, expr, alarmKey);
                                }
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    } catch (Exception ignored) {}
                }
                for (CollectRowsParams valueRow : metricsData.getValue()) {
                    if (CollectionUtils.isEmpty(valueRow.getColumns())) {
                        continue;
                    }
                    fieldValueMap.clear();
                    fieldValueMap.put(SYSTEM_VALUE_ROW_COUNT, valueRowCount);
                    StringBuilder tagBuilder = new StringBuilder();
                    for (int index = 0; index < valueRow.getColumnsCount(); index++) {
                        String valueStr = valueRow.getColumns().get(index);
                        if (AlerterContext.NULL_VALUE.equals(valueStr)) {
                            continue;
                        }

                        final CollectFieldParams field = fields.get(index);
                        final int fieldType = field.getType();

                        if (fieldType == AlerterContext.TYPE_NUMBER) {
                            final Double doubleValue;
                            if ((doubleValue = AlerterUtil.parseStrDouble(valueStr)) != null) {
                                fieldValueMap.put(field.getName(), doubleValue);
                            }
                        } else if (fieldType == AlerterContext.TYPE_TIME) {
                            final Integer integerValue;
                            if ((integerValue = AlerterUtil.parseStrInteger(valueStr)) != null) {
                                fieldValueMap.put(field.getName(), integerValue);
                            }
                        } else {
                            if (StringUtils.isNotEmpty(valueStr)) {
                                fieldValueMap.put(field.getName(), valueStr);
                            }
                        }

                        if (field.isLabel()) {
                            tagBuilder.append(Constants.MIDDLE_LINE).append(valueStr);
                        }
                    }
                    try {
                        boolean match = execAlertExpression(fieldValueMap, expr);
                        try {
                            if (match) {
                                // If the threshold rule matches, the number of times the threshold has been triggered is determined and an alarm is triggered
                                // 阈值规则匹配，判断已触发阈值次数，触发告警
                                afterThresholdRuleMatch(ldt, monitorId, app, metrics, tagBuilder.toString(), fieldValueMap, define);
                                // 若此阈值已被触发，则其它数据行的触发忽略
                                break;
                            } else {
                                String alarmKey = monitorId + define.getId() + tagBuilder;
                                triggeredAlerterMap.remove(alarmKey);
                                if (define.isRecoverNotice()) {
                                    handleRecoveredAlert(ldt, define, expr, alarmKey);
                                }
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    private void handlerAvailableMetrics(String monitorId, String app, CollectMetricsParams metricsData) {
        if (metricsData.getCode() == CollectCode.TIMEOUT) {
            return;
        }
        // TODO CACHE getMonitorBindAlertAvaDefine
        AlerterDefineInfo avaAlertDefine = alerterDataService.monitorBindAlerterAvaDefine(monitorId, app, AlerterContext.AVAILABILITY);
        if (avaAlertDefine == null) {
            return;
        }
        LocalDateTime ldt = LocalDateTime.now();
        if (metricsData.getCode() != CollectCode.SUCCESS) {
            NotifierAlerterInfo preAlert = triggeredAlerterMap.get(monitorId);
            Map<String, String> tags = new HashMap<>(16);
            tags.put(TagsContext.TAG_MONITOR_ID, monitorId);
            tags.put(TagsContext.TAG_MONITOR_APP, app);
            tags.put(TagsContext.TAG_THRESHOLD_ID, avaAlertDefine.getId());
            tags.put(TagsContext.TAG_METRICS, AlerterContext.AVAILABILITY);
            tags.put(TagsContext.TAG_CODE, metricsData.getCode().name());
            List<AlerterTagItemInfo> tagItems = avaAlertDefine.getTags();
            if (CollectionUtils.isNotEmpty(tagItems)) {
                tagItems.forEach(t -> tags.put(t.getName(), t.getValue()));
            }
            Map<String, Object> valueMap = new HashMap<>(16);
            valueMap.putAll(tags);

            if (preAlert == null) {
                NotifierAlerterInfo alertBuilder = builderAlerter(AlerterContext.AVAILABILITY, avaAlertDefine.getPriority(), Constants.STATUS_01, AlerterUtil.render(avaAlertDefine.getTemplate(), valueMap),
                        ldt, 1, tags);
                if (avaAlertDefine.getTimes() == null || avaAlertDefine.getTimes() <= 1) {
                    String notResolvedAlertKey = monitorId + AlerterContext.AVAILABILITY;
                    alertBuilder.setDataStatus(Constants.STATUS_00);
                    notRecoveredAlerterMap.put(notResolvedAlertKey, alertBuilder);
                    alerterCommonReduce.reduceAndSendAlarm(alertBuilder);
                } else {
                    triggeredAlerterMap.put(monitorId, alertBuilder);
                }
            } else {
                int times = preAlert.getTriggerTimes() + 1;
                preAlert.setTriggerTimes(times);
                preAlert.setFirstAlerterTime(ldt);
                preAlert.setAlerterTime(ldt);
                int defineTimes = ObjectUtils.defaultIfNull(avaAlertDefine.getTimes(), 1);
                if (times >= defineTimes) {
                    preAlert.setDataStatus(Constants.STATUS_00);
                    NotifierAlerterInfo clonePre = AlerterUtil.clone(preAlert);
                    String notResolvedAlertKey = monitorId + AlerterContext.AVAILABILITY;
                    notRecoveredAlerterMap.put(notResolvedAlertKey, clonePre);
                    alerterCommonReduce.reduceAndSendAlarm(clonePre);
                    triggeredAlerterMap.remove(monitorId);
                }
            }
        } else {
            // Check whether an availability or unreachable alarm is generated before the association monitoring
            // and send a clear alarm to clear the monitoring status
            // 判断关联监控之前是否有可用性或者不可达告警,发送恢复告警进行任务状态恢复
            triggeredAlerterMap.remove(monitorId);
            String notResolvedAlertKey = monitorId + AlerterContext.AVAILABILITY;
            NotifierAlerterInfo notResolvedAlert = notRecoveredAlerterMap.remove(notResolvedAlertKey);
            if (notResolvedAlert != null) {
                // Sending an alarm Restore
                Map<String, String> tags = notResolvedAlert.getTags();
                if (!avaAlertDefine.isRecoverNotice()) {
                    tags.put(TagsContext.IGNORE, TagsContext.IGNORE);
                }
                NotifierAlerterInfo resumeAlert = builderAlerter(AlerterContext.AVAILABILITY, Constants.STATUS_02, Constants.STATUS_02, AlerterContext.RECOVER,
                        ldt,  notResolvedAlert.getAlerterTime(),1, tags);
                alerterCommonReduce.reduceAndSendAlarm(resumeAlert);
                AlerterThread.execute(() -> updateAvailabilityAlerterStatus(monitorId, resumeAlert));
            }
        }
    }

    private void updateAvailabilityAlerterStatus(String monitorId, NotifierAlerterInfo restoreAlert) {
        List<NotifierAlerterInfo> list = alerterDataService.alerterLike(monitorId, Constants.STATUS_00, Constants.STATUS_00, DateUtil.formatDateTime(restoreAlert.getAlerterTime(), null));
        List<String> idList = PojoUtil.toList(list, NotifierAlerterInfo::getId);
        alerterDataService.updateAlerterStatus(Constants.STATUS_03, idList);
    }

    private boolean execAlertExpression(Map<String, Object> fieldValueMap, String expr) {
        Boolean match;
        try {
            Expression expression = AviatorEvaluator.compile(expr, true);
            expression.getVariableNames().forEach(variable -> {
                if (!fieldValueMap.containsKey(variable)) {
                    throw new ExpressionRuntimeException("metrics value not contains expr field: " + variable);
                }
            });
            match = (Boolean) expression.execute(fieldValueMap);
        } catch (CompileExpressionErrorException |
                 ExpressionSyntaxErrorException compileException) {
            log.error("Alert Define Rule: {} Compile Error: {}.", expr, compileException.getMessage());
            throw compileException;
        } catch (ExpressionRuntimeException expressionRuntimeException) {
            log.error("Alert Define Rule: {} Run Error: {}.", expr, expressionRuntimeException.getMessage());
            throw expressionRuntimeException;
        } catch (Exception e) {
            log.error("Alert Define Rule: {} Unknown Error: {}.", expr, e.getMessage());
            throw e;
        }
        return match != null && match;
    }

    private void afterThresholdRuleMatch(LocalDateTime ldt, String monitorId, String app, String metrics, String tagStr,
                                         Map<String, Object> fieldValueMap, AlerterDefineInfo define) {
        String alarmKey = monitorId + define.getId() + tagStr;
        NotifierAlerterInfo triggeredAlerter = triggeredAlerterMap.get(alarmKey);
        if (triggeredAlerter != null) {
            int times = triggeredAlerter.getTriggerTimes() + 1;
            triggeredAlerter.setAlerterTimes(times);
            triggeredAlerter.setFirstAlerterTime(ldt);
            triggeredAlerter.setAlerterTime(ldt);
            int defineTimes = ObjectUtils.defaultIfNull(define.getTimes(), 1);
            if (times >= defineTimes) {
                triggeredAlerter.setDataStatus(Constants.STATUS_00);
                triggeredAlerterMap.remove(alarmKey);
                notRecoveredAlerterMap.put(alarmKey, triggeredAlerter);
                alerterCommonReduce.reduceAndSendAlarm(AlerterUtil.clone(triggeredAlerter));
            }
        } else {
            fieldValueMap.put(TagsContext.TAG_MONITOR_APP, app);
            fieldValueMap.put(TagsContext.TAG_METRICS, metrics);
            fieldValueMap.put(TagsContext.TAG_METRIC, define.getField());
            Map<String, String> tags = new HashMap<>(16);
            tags.put(TagsContext.TAG_MONITOR_ID, monitorId);
            tags.put(TagsContext.TAG_MONITOR_APP, app);
            tags.put(TagsContext.TAG_THRESHOLD_ID, define.getId());
            if (CollectionUtils.isNotEmpty(define.getTags())) {
                for (AlerterTagItemInfo tagItem : define.getTags()) {
                    fieldValueMap.put(tagItem.getName(), tagItem.getValue());
                    tags.put(tagItem.getName(), tagItem.getValue());
                }
            }
            NotifierAlerterInfo alert = builderAlerter(app + Constants.SPOT + metrics + Constants.SPOT + define.getField(), define.getPriority(), Constants.STATUS_01, AlerterUtil.render(define.getTemplate(), fieldValueMap),
                    ldt, 1, tags);
            int defineTimes = ObjectUtils.defaultIfNull(define.getTimes(), 1);
            if (1 >= defineTimes) {
                alert.setDataStatus(Constants.STATUS_00);
                notRecoveredAlerterMap.put(alarmKey, alert);
                alerterCommonReduce.reduceAndSendAlarm(AlerterUtil.clone(alert));
            } else {
                triggeredAlerterMap.put(alarmKey, alert);
            }
        }
    }

    private void handleRecoveredAlert(LocalDateTime ldt, AlerterDefineInfo define, String expr, String alarmKey) {
        NotifierAlerterInfo notResolvedAlert = notRecoveredAlerterMap.remove(alarmKey);
        if (notResolvedAlert != null) {
            // Sending an alarm Restore
            String content = AlerterContext.RECOVER + " : " + expr;
            String target = define.getApp() + Constants.SPOT + define.getMetric() + Constants.SPOT + define.getField();
            NotifierAlerterInfo resumeAlert = builderAlerter(target, Constants.STATUS_02, Constants.STATUS_02, content,
                    ldt, null, notResolvedAlert.getTags());
            alerterCommonReduce.reduceAndSendAlarm(resumeAlert);
        }
    }

    private NotifierAlerterInfo builderAlerter(String target, String priority, String status, String content,
                                               LocalDateTime ldt, Integer times, Map<String, String> tags) {
        return builderAlerter(target, priority, status, content, ldt, ldt, times, tags);
    }

    private NotifierAlerterInfo builderAlerter(String target, String priority, String status, String content,
                                               LocalDateTime firstLdt, LocalDateTime ldt, Integer times, Map<String, String> tags) {
        NotifierAlerterInfo alert = new NotifierAlerterInfo();
        alert.setTags(tags);
        alert.setAlerterTime(firstLdt);
        alert.setFirstAlerterTime(ldt);
        alert.setPriority(priority);
        alert.setDataStatus(status);
        alert.setContent(content);
        alert.setTarget(target);
        if (times != null) {
            alert.setAlerterTimes(times);
        }
        return alert;
    }
}
