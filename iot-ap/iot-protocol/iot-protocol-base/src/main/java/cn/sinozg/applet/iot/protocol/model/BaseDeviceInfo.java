package cn.sinozg.applet.iot.protocol.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.validation.constraints.NotBlank;

/**
 * 设备基本信息
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-11-15 21:57:47
 */
@Getter
@Setter
@FieldNameConstants
public abstract class BaseDeviceInfo {
    @NotBlank(message = "产品key不能为空")
    @Schema(description = "产品key")
    private String prodKey;
    @Schema(description = "设备code")
    @NotBlank(message = "设备code不能为空")
    private String deviceCode;
}