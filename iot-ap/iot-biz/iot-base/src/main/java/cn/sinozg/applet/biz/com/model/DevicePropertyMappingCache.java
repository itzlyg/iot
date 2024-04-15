package cn.sinozg.applet.biz.com.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 设备缓存
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-12-08 21:19:32
 */
@Data
public class DevicePropertyMappingCache {

    @Schema(description = "属性值")
    private Object value;

    @Schema(description = "属性值时间: 设备上报时间")
    private long occurred;
}
