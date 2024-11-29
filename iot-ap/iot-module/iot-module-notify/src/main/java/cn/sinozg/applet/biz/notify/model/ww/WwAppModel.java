package cn.sinozg.applet.biz.notify.model.ww;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-19 20:32
 */
@Data
public class WwAppModel {

    @JsonProperty(value = "touser")
    private String toUser;

    @JsonProperty(value = "toparty")
    private String toParty;

    @JsonProperty(value = "totag")
    private String toTag;

    @JsonProperty(value = "msgtype")
    private String msgType = "markdown";

    @JsonProperty(value = "agentid")
    private Integer agentId;

    @Schema(description = "text 消息")
    private WwContent text;

    @Schema(description = "markdown消息")
    private WwContent markdown;
}
