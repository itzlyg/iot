package cn.sinozg.applet.biz.device.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-20 17:36
 */
@Data
public class DeviceOrderGetPropertyRequest {

    @NotBlank(message = "设备id不能为空")
    @Schema(description = "设备id")
    private String deviceId;

    @Schema(description = "任务名称")
    private String sendName;

    @Schema(description = "设备属性")
    @NotNull(message = "设备属性不能为空！")
    private List<String> properties;
}
