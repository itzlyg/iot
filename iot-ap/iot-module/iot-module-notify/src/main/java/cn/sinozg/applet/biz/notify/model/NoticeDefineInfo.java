package cn.sinozg.applet.biz.notify.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-29 17:16
 */
@Data
public class NoticeDefineInfo {

    /** 主键id */
    @Schema(description = "主键id")
    private String id;


    /** 名称 */
    @Schema(description = "名称")
    private String name;


    /** 产品id */
    @Schema(description = "产品id")
    private String prodId;


    /** 设备集合 */
    @Schema(description = "设备集合 空，表示所有设备")
    private List<String> deviceIds;


    /** 告警表达式 */
    @Schema(description = "告警表达式")
    private String expr;


    /** 告警级别 */
    @Schema(description = "告警级别")
    private String priority;


    /** 触发次数,即达到触发阈值次数要求后才算触发告警 */
    @Schema(description = "触发次数,即达到触发阈值次数要求后才算触发告警")
    private Integer times;


    /** 告警标签 */
    @Schema(description = "告警标签")
    private Map<String, String> tags;


    /** 是否全局默认告警 */
    @Schema(description = "是否全局默认告警")
    private Boolean preset;


    /** 是否发送告警恢复通知 */
    @Schema(description = "是否发送告警恢复通知")
    private Boolean recoverNotice;


    /** 模板内容 */
    @Schema(description = "模板内容")
    private String template;


    /** 接收人 */
    @Schema(description = "接收人")
    private List<String> toUser;

    /** 告警阈值开关 */
    @Schema(description = "告警阈值开关")
    private Boolean enable;

    @Schema(description = "匹配规则")
    private List<NoticeConditionInfo> conditions;
}
