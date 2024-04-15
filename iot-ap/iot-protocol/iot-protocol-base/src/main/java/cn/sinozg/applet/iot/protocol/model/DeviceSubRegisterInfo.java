package cn.sinozg.applet.iot.protocol.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 自设备注册信息
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-15 21:58
 */
@Getter
@Setter
public class DeviceSubRegisterInfo extends BaseDeviceInfo {

    @Schema(description = "model")
    private String model;

    @Schema(description = "tag 标签")
    private Map<String, Object> tag;
}
