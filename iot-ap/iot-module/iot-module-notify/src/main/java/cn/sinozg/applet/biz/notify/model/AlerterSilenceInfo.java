package cn.sinozg.applet.biz.notify.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 策略
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-20 13:42
 */
@Setter
@Getter
public class AlerterSilenceInfo extends AlerterBaseInfo {

    @Schema(description = "静默类型 01:一次性静默 02:周期性静默")
    private String type;

    @Schema(description = "告警次数")
    private Integer alerterTimes;

    @Schema(description = "周期性静默时有效 星期几,多选,全选或空则为每天 7:周日 1:周一 2:周二 3:周三 4:周四 5:周五 6:周六")
    private List<Integer> days;

    @Schema(description = "限制时间段起始")
    private LocalDateTime periodStart;

    @Schema(description = "限制时间段起截止")
    private LocalDateTime periodEnd;
}
