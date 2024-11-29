package cn.sinozg.applet.biz.notify.model.freemark;

import cn.sinozg.applet.biz.notify.core.AlerterContext;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * 封装 Freemarker 参数
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-19 15:23
 */
@Data
public class FreemarkerModel {
    /** 可配置的 先固定值 左边的标题 */

    @Schema(description = "任务id 名称")
    private String labelMonitorId = AlerterContext.LABEL_MONITOR_ID;
    @Schema(description = "任务名称 名称")
    private String labelMonitorName = AlerterContext.LABEL_MONITOR_NAME;
    @Schema(description = "目标对象 名称")
    private String labelTarget = AlerterContext.LABEL_TARGET;
    @Schema(description = "告警级别 名称")
    private String labelPriority = AlerterContext.LABEL_PRIORITY;
    @Schema(description = "告警触发时间 名称")
    private String labelTriggerTime = AlerterContext.LABEL_TRIGGER_TIME;
    @Schema(description = "告警恢复时间 名称")
    private String labelRestoreTime = AlerterContext.LABEL_RESTORE_TIME;
    @Schema(description = "告警次数 名称")
    private String labelTimes = AlerterContext.LABEL_TIMES;
    @Schema(description = "标签 名称")
    private String labelTags = AlerterContext.LABEL_TAGS;
    @Schema(description = "控制台 名称")
    private String labelConsole = AlerterContext.LABEL_CONSOLE;
    @Schema(description = "内容 名称")
    private String labelContent = AlerterContext.LABEL_CONTENT;

    @Schema(description = "标题")
    private String title = AlerterContext.TITLE;
    @Schema(description = "目标对象")
    private String target;
    @Schema(description = "任务id")
    private String monitorId;
    @Schema(description = "任务名称")
    private String monitorName;
    @Schema(description = "告警级别")
    private String priority;
    @Schema(description = "告警状态")
    private String status;
    @Schema(description = "告警触发时间")
    private String triggerTime;
    @Schema(description = "告警恢复时间")
    private String restoreTime;
    @Schema(description = "url")
    private String url;
    @Schema(description = "内容")
    private String content;
    @Schema(description = "告警次数")
    private Integer alerterTimes;
    @Schema(description = "标签")
    private Map<String, String> tags;
}
