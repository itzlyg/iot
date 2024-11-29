package cn.sinozg.applet.biz.notify.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 策略
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-20 13:42
 */
@Setter
@Getter
public class AlerterConvergeInfo extends AlerterBaseInfo {

    @Schema(description = "重复时间间隔 s")
    private Integer evalInterval;
}
