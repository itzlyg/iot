package cn.sinozg.applet.iot.protocol.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 设备基本信息
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-11-15 21:57:47
 */
@Getter
@Setter
@Schema(description = "设备信息")
public class DeviceBaseInfo extends BaseDeviceInfo {
}