package cn.sinozg.applet.biz.notify.core;

/**
 * 告警标签字段
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-19 15:43
 */
public interface TagsContext {
    String IGNORE = "ignore";
    /**
     * 内有标签: monitorId 监控任务ID
     */
    String TAG_MONITOR_ID = "monitorId";
    /**
     * 内有标签: policyId 告警阈值规则ID
     */
    String TAG_THRESHOLD_ID = "thresholdId";

    /**
     * 内有标签: app 监控类型
     */
    String TAG_MONITOR_APP = "app";

    /**
     * 内有标签: monitorName 任务名称
     */
    String TAG_MONITOR_NAME = "monitorName";

    /**
     * 内有标签: metrics
     */
    String TAG_METRICS = "metrics";

    /**
     * 内有标签: metric
     */
    String TAG_METRIC = "metric";

    /**
     * 内有标签: code
     */
    String TAG_CODE = "code";

}
