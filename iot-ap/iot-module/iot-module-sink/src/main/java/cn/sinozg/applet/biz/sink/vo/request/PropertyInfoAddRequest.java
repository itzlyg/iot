package cn.sinozg.applet.biz.sink.vo.request;

import cn.sinozg.applet.biz.com.model.DevicePropertyMappingCache;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-05 20:19
 */
@Data
public class PropertyInfoAddRequest {

    @Schema(description = "设备编号")
    private String deviceCode;

    @Schema(description = "属性上报时间")
    private long time;

    private String prodKey;

    private Map<String, DevicePropertyMappingCache> details;
}
