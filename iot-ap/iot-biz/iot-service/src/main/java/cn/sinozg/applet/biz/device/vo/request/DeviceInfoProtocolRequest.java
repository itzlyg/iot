package cn.sinozg.applet.biz.device.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-05 16:09
 */
@Data
public class DeviceInfoProtocolRequest {

    private String prodKey;

    private String deviceCode;

    @Schema(description = "判断为空")
    private boolean notNull;
}
