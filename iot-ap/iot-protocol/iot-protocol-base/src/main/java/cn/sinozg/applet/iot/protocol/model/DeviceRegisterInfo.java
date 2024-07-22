package cn.sinozg.applet.iot.protocol.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.List;
import java.util.Map;

/**
 * 设备注册信息
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-11-15 21:57:47
 */
@Getter
@Setter
@FieldNameConstants
@Schema(description = "设备注册 type 为 register")
public class DeviceRegisterInfo extends BaseDeviceInfo {

    @Schema(description = "model")
    private String model;

    @Schema(description = "tag 标签")
    private Map<String, Object> tag;

    @Schema(description = "子设备")
    private List<DeviceSubRegisterInfo> subDevices;
}