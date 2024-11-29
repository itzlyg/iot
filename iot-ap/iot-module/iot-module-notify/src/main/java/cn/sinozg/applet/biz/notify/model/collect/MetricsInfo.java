package cn.sinozg.applet.biz.notify.model.collect;

import cn.sinozg.applet.biz.notify.model.proto.CollectMetricsParams;
import cn.sinozg.applet.biz.notify.model.proto.CollectRowsParams;
import cn.sinozg.applet.biz.notify.model.protocol.JdbcProtocol;
import cn.sinozg.applet.common.utils.PojoUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Description
 * @Copyright Copyright (c) 2024
 * @author xieyubin
 * @since 2024-01-22 16:59:36
 */
@Slf4j
@Data
public class MetricsInfo implements Serializable {

    /**
     * public property-name eg: cpu | memory | health
     */
    private String name;
    /**
     * metrics name's i18n value
     * zh-CN: CPU信息
     * en-US: CPU Info
     */
    private Map<String, String> i18n;
    /**
     * collect protocol eg: sql, ssh, http, telnet, wmi, snmp, sdk
     */
    private String protocol;
    /**
     * Range (0-127) metrics scheduling priority, the smaller the value, the higher the priority
     * The collection task of the next priority metrics will be scheduled only after the scheduled collection with the higher priority is completed.
     * The default priority of the availability metrics is 0, and the range of other common metrics is 1-127, that is,
     * the subsequent metrics tasks will only be scheduled after the availability is collected successfully.
     * 范围(0-127)调度优先级,数值越小优先级越高
     * 优先级高的调度采集完成后才会调度下一优先级的采集任务
     * 可用性指标(availability)默认优先级为0,其它普通指标采集范围为1-127,即需要等availability采集成功后才会调度后面的任务
     */
    private Byte priority;
    /**
     * Is it visible true or false
     * if false, web ui will not see this metrics.
     */
    private boolean visible = true;
    /**
     * Public attribute - collection and monitoring final result attribute set eg: speed | times | size
     * 公共属性-采集监控的最终结果属性集合 eg: speed | times | size
     */
    private List<MetricsField> fields;
    /**
     * Public attribute - collection and monitoring pre-query attribute set eg: size1 | size2 | speedSize
     * 公共属性-采集监控的前置查询属性集合 eg: size1 | size2 | speedSize
     */
    private List<String> aliasFields;
    /**
     * Public attribute - expression calculation, map the pre-query attribute (pre Fields) with the final attribute (fields), and calculate the final attribute (fields) value
     * 公共属性-表达式计算，将前置查询属性(preFields)与最终属性(fields)映射,计算出最终属性(fields)值
     * eg: size = size1 + size2, speed = speedSize
     * <a href="https://www.yuque.com/boyan-avfmj/aviatorscript/ban32m">www.yuque.com/boyan-avfmj/aviatorscript/ban32m</a>
     */
    private List<String> calculates;
    /**
     * unit conversion expr
     * eg:
     * - heap_used=B->MB
     * - heap_total=B->MB
     * - disk_free=B->GB
     * - disk_total=B->GB
     */
    private List<String> units;

    /**
     * Database configuration information implemented using the public jdbc specification
     * 使用公共的jdbc规范实现的数据库配置信息
     */
    private JdbcProtocol jdbc;


    /**
     * collector use - Temporarily store subTask metrics response data
     * collector使用 - 临时存储分级任务指标响应数据
     */
    @JsonIgnore
    private transient AtomicReference<CollectMetricsParams> subTaskDataRef;

    /**
     * collector use - Temporarily store subTask running num
     * collector使用 - 分级任务正在运行中的数量
     */
    @JsonIgnore
    private transient AtomicInteger subTaskNum;

    /**
     * collector use - Temporarily store subTask id
     * collector使用 - 分级采集任务ID
     */
    @JsonIgnore
    private transient Integer subTaskId;

    /**
     * is has subTask
     *
     * @return true - has
     */
    public boolean isHasSubTask() {
        return subTaskNum != null;
    }

    /**
     * consume subTask
     *
     * @param metricsData response data
     * @return is last task?
     */
    public boolean consumeSubTaskResponse(CollectMetricsParams metricsData) {
        if (subTaskNum == null) {
            return true;
        }
        synchronized (subTaskNum) {
            int index = subTaskNum.decrementAndGet();
            if (subTaskDataRef.get() == null) {
                subTaskDataRef.set(metricsData);
            } else {
                if (CollectionUtils.isNotEmpty(metricsData.getValue())) {
                    CollectMetricsParams metricsParams = PojoUtil.copyBean(subTaskDataRef.get(), CollectMetricsParams.class);
                    for (CollectRowsParams valueRow : metricsData.getValue()) {
                        if (valueRow.getColumnsCount() == metricsParams.getFieldsCount()) {
                            PojoUtil.setBeanList(metricsParams, valueRow, CollectMetricsParams::getValue, CollectMetricsParams::setValue);
                        } else {
                            log.error("consume subTask data value not mapping filed");
                        }
                    }
                    subTaskDataRef.set(metricsParams);
                }
            }
            return index == 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MetricsInfo metrics = (MetricsInfo) o;
        return name.equals(metrics.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
