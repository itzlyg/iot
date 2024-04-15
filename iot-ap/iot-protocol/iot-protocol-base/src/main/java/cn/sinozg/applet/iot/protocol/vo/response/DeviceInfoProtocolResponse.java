package cn.sinozg.applet.iot.protocol.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-16 11:01
 */
@Data
public class DeviceInfoProtocolResponse {

    private String id;

    @Schema(description = "设备编码")
    private String deviceCode;

    @Schema(description = "产品key")
    private String prodKey;

    @Schema(description = "设备名称")
    private String deviceName;

    @Schema(description = "设备型号")
    private String model;

    @Schema(description = "设备密钥")
    private String secret;

    @Schema(description = "父id")
    private String parentId;

    @Schema(description = "所属平台用户ID")
    private String uid;

    /**
     * 关联子用户ID列表
     */
    private List<String> subUid = new ArrayList<>();

    @Schema(description = "设备定位对象")
    private DeviceLocateProtocolResponse locate = new DeviceLocateProtocolResponse();

    @Schema(description = "设备在离线状态 true 在线")
    private Boolean online;

    /**
     * 设备属性
     */
    private Map<String, ?> property = new HashMap<>();

    /**
     * 设备标签
     */
    private Map<String, DeviceTagProtocolResponse> tag = new HashMap<>();

    /**
     * 设备所属分组
     */
    private Map<String, DeviceGroupProtocolResponse> group = new HashMap<>();
}
