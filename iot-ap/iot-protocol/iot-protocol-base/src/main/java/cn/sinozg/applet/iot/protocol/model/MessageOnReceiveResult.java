package cn.sinozg.applet.iot.protocol.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * onReceive 返回消息接收实体
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-10 15:31
 */
@Data
public class MessageOnReceiveResult {

    @Schema(description = "消息类型 register：注册类 auth：授权 state：设备状态变更 report：上报 ota：ota")
    private String type;

    @Schema(description = "action ")
    private MessageAction action;

    @Schema(description = "具体信息 根据type类型不同返回不同的对象")
    private Map<String, Object> data;
}
