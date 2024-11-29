package cn.sinozg.applet.biz.notify.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 规则信息
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-19 14:52
 */
@Data
public class NotifierRuleInfo {

    @Schema(description = "模版id")
    private String templateId;

    @Schema(description = "接收配置id")
    private String configId;

    @Schema(description = "规则定义id")
    private String defineId;

    @Schema(description = "是否启用此策略")
    private boolean enable = true;

    @Schema(description = "是否转发所有")
    private boolean filterAll = true;

    @Schema(description = "过滤匹配告警级别，空为全部告警级别 0:高-emergency-紧急告警-红色 1:中-critical-严重告警-橙色 2:低-warning-警告告警-黄色")
    private List<String> priorities;

    @Schema(description = "告警信息标签(monitorId:xxx,monitorName:xxx)")
    private List<AlerterTagItemInfo> tags;

    @Schema(description = "星期几,多选,全选或空则为每天 7:周日 1:周一 2:周二 3:周三 4:周四 5:周五 6:周六")
    private List<Integer> days;

    @Schema(description = "限制时间段起始")
    private LocalDateTime periodStart;

    @Schema(title = "限制时间段截止")
    private LocalDateTime periodEnd;
}
