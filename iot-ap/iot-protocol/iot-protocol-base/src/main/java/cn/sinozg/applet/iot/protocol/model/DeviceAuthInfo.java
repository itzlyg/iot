package cn.sinozg.applet.iot.protocol.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 设备权限信息
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-11-15 22:27:40
 */
@Getter
@Setter
@Schema(description = "设备认证 type 为 auth")
public class DeviceAuthInfo extends BaseDeviceInfo {
    @Schema(description = "产品密钥")
    private String prodSecret;
    @Schema(description = "设备密钥")
    private String deviceSecret;

}
