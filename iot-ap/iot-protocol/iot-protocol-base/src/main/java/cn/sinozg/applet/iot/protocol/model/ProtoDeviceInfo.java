package cn.sinozg.applet.iot.protocol.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * 设备信息
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-15 17:05
 */
@Data
public class ProtoDeviceInfo {

    @Schema(description = "设备编号")
    @NotNull(message = "设备编号不能为空")
    private String deviceCode;

    @Schema(description = "型号")
    @NotNull(message = "型号不能为空")
    private String model;

    @Schema(description = "设备属性")
    private Map<String, ?> property = new HashMap<>();

    @Schema(description = "tag 设备标签")
    private Map<String, ?> tag = new HashMap<>();

    @Schema(description = "是否透传")
    private Boolean transparent;
}
