package cn.sinozg.applet.biz.notify.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-20 16:50
 */
@Data
public class AlerterDefineInfo {

    private String id;
    @Schema(description = "Monitoring Type linux")
    private String app;

    @Schema(description = "Monitoring Metrics cpu")
    private String metric;

    private String expr;
    @Schema(description = "告警级别 01:紧急告警 02:严重告警 03:警告告警")
    private String priority;

    @Schema(description = "阈值触发次数,即达到次数要求后才触发告警")
    private Integer times;

    @Schema(description = "字段")
    private String field;

    @Schema(description = "模板")
    private String template;

    @Schema(description = "是否发送告警恢复通知")
    private boolean recoverNotice = false;

    private List<AlerterTagItemInfo> tags;
}
