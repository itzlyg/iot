package cn.sinozg.applet.iot.protocol.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 设备具体信息
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-15 16:57
 */
@Getter
@Setter
@Schema(description = "设备上报 信息")
public class DeviceMessageInfo extends BaseDeviceInfo {

    @Schema(description = "mid，请求id")
    @NotBlank(message = "请求id不能为空")
    private String mid;

    @Schema(description = "发送topic")
    @NotBlank(message = "发送topic不能为空")
    private String topic;

    @Schema(description = "订阅topic 可为空")
    @NotBlank(message = "订阅topic不能为空")
    private String subscribeTopic;

    @Schema(description = "发送的信息 对象 mqtt 指令 或者其他数据")
    @NotNull(message = "发送的信息不能为空")
    private Object content;
}
