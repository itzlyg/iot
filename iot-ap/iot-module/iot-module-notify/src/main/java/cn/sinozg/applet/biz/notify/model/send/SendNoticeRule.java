package cn.sinozg.applet.biz.notify.model.send;

import cn.sinozg.applet.biz.notify.model.NotifierAlerterInfo;
import cn.sinozg.applet.biz.notify.model.NotifierTemplateInfo;
import cn.sinozg.applet.biz.notify.model.channel.NotifierConfigBaseInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalTime;

/**
 * 发送规则
 * @Author: xyb
 * @Description:
 * @Date: 2024-02-18 下午 10:01
 **/
@Data
public class SendNoticeRule {

    @Schema(description = "id")
    private String id;

    /** 规则名称 */
    @Schema(description = "规则名称")
    private String ruleName;

    /** 通道id */
    @Schema(description = "通道id")
    private String channelId;

    /** 通道名称 */
    @Schema(description = "通道名称")
    private String channelName;

    /** 模板id */
    @Schema(description = "模板id")
    private String templateId;

    /** 模板名称 */
    @Schema(description = "模板名称")
    private String templateName;

    /** 规则定义id */
    @Schema(description = "规则定义id")
    private String defineId;

    /** 告警级别，空全部 */
    @Schema(description = "告警级别，空全部")
    private String priorities;

    /** 匹配告警信息标签(monitorId:xxx,monitorName:xxx) */
    @Schema(description = "匹配告警信息标签(monitorId:xxx,monitorName:xxx)")
    private String tags;

    /** 星期几,多选,全选或空则为每天 7:周日 1:周一 2:周二 3:周三 4:周四 5:周五 6:周六 */
    @Schema(description = "星期几,多选,全选或空则为每天 7:周日 1:周一 2:周二 3:周三 4:周四 5:周五 6:周六")
    private String days;

    /** 限制时间段起始 */
    @Schema(description = "限制时间段起始")
    private LocalTime periodStart;

    /** 限制时间段截止 */
    @Schema(description = "限制时间段截止")
    private LocalTime periodEnd;

    /** 转发所有 */
    @Schema(description = "转发所有")
    private Boolean filterAll;

    @Schema(description = "模板信息")
    private NotifierTemplateInfo template;

    @Schema(description = "预警配置")
    private NotifierAlerterInfo alerter;

    @Schema(description = "渠道配置信息")
    private NotifierConfigBaseInfo config;
}