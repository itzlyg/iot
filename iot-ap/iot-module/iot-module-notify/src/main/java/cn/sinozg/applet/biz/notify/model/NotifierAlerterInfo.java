package cn.sinozg.applet.biz.notify.model;

import cn.sinozg.applet.common.enums.DictType;
import cn.sinozg.applet.common.utils.DictUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 告警信息
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-19 14:52
 */
@Data
public class NotifierAlerterInfo {

    private String id;

    @Schema(description = "告警级别 01:紧急告警 02:严重告警 03:警告告警")
    private String priority;

    @Schema(description = "目标")
    private String target;

    @Schema(description = "告警次数")
    private Integer alerterTimes;

    @Schema(description = "告警阈值触发次数")
    private Integer triggerTimes;

    @Schema(description = "告警状态 ，00 正常告警(待处理) 01 阈值触发但未达到告警次数 02 恢复告警 03 已处理")
    private String dataStatus;

    @Schema(description = "第一次告警时间")
    private LocalDateTime firstAlerterTime;

    @Schema(description = "最后告警时间")
    private LocalDateTime alerterTime;

    @Schema(description = "恢复时间")
    private LocalDateTime restoreTime;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "告警信息标签(monitorId:xxx,monitorName:xxx)")
    private Map<String, String> tags;

    @Schema(description = "接收人 邮件、手机、企业微信app")
    private List<String> toUser;

    public String getPriorityName() {
        return DictUtil.getDictLabel(DictType.ALERT_PRIORITY, priority);
    }
}
