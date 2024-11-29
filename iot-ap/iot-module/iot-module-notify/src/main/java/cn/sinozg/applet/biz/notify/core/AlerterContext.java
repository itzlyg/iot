package cn.sinozg.applet.biz.notify.core;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-19 15:28
 */
public interface AlerterContext {

    String TITLE = "系统告警通知";

    String RECOVER = "可用性告警恢复通知, 任务状态已恢复正常";

    String LABEL_MONITOR_ID = "监控任务ID";

    String LABEL_MONITOR_NAME = "监控任务名称";
    String LABEL_TARGET = "告警目标对象";

    String LABEL_PRIORITY = "告警级别";

    String LABEL_TRIGGER_TIME = "告警触发时间";

    String LABEL_RESTORE_TIME = "告警恢复时间";
    String LABEL_CONSOLE = "跳转系统";
    String LABEL_TAGS = "告警标签";
    String LABEL_TIMES = "告警次数";
    String LABEL_CONTENT = "内容详情";

    /**
     * for prometheus task name prefix
     */
    String PROMETHEUS_APP_PREFIX = "_prometheus_";

    /**
     * prometheus
     */
    String PROMETHEUS = "prometheus";

    String NULL_VALUE = "&nbsp;";

    /**
     * Availability 监控总可用性指标
     */
    String AVAILABILITY = "availability";
    /**
     * Field parameter type: number
     * 字段参数类型: 数字
     */
    byte TYPE_NUMBER = 0;

    /**
     * Field parameter type: String
     * 字段参数类型: 字符串
     */
    byte TYPE_STRING = 1;

    /**
     * Field parameter type: encrypted string
     * 字段参数类型: 加密字符串
     */
    byte TYPE_SECRET = 2;

    /**
     * Field parameter type: time
     * 字段参数类型: 时间
     */
    byte TYPE_TIME = 3;

    int DISPATCH_THREADS = 3;

    /**
     * Parameter Type String
     * 参数类型 字符串
     */
    byte PARAM_TYPE_STRING = 1;

    /**
     * Parameter Type Password
     * 参数类型 密码
     */
    byte PARAM_TYPE_PASSWORD = 2;

    /**
     * Parameter Type Map values
     */
    byte PARAM_TYPE_MAP = 3;

    /**
     * Parameter Type arrays values
     */
    byte PARAM_TYPE_ARRAY = 4;
    String PROM_TIME = "timestamp";

    /**
     *
     */
    String PROM_VALUE = "value";
}
