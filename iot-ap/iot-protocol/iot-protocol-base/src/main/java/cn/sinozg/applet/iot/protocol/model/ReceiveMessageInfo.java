package cn.sinozg.applet.iot.protocol.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 接收到的消息
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-11-15 16:48:28
 */
@Getter
@Setter
public class ReceiveMessageInfo extends BaseDeviceInfo {
    @Schema(description = "数据")
    private Object data;
}
