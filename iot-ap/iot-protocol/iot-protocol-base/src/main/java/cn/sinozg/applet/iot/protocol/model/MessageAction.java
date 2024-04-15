package cn.sinozg.applet.iot.protocol.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-15 21:54
 */
@Data
@Schema(name = "MessageAction", description = "action")
public class MessageAction {

    @Schema(description = "消息类型 如果是 ack 再次发送")
    private String type;

    @Schema(description = "具体信息 json 对应的是 DeviceMessageInfo")
    private String content;
}
