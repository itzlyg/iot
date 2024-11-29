package cn.sinozg.applet.biz.notify.model.ww;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-19 20:15
 */
@Data
public class WwRobotModel {

    @JsonProperty(value = "msgtype")
    @Schema(description = "消息类型")
    private String msgType = "markdown";

    @Schema(description = "markdown消息")
    private WwContent markdown;
}
