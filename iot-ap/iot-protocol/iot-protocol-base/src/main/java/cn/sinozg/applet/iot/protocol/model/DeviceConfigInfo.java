package cn.sinozg.applet.iot.protocol.model;

import lombok.Data;

/**
 * 设备配置信息
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-11-16 20:40:09
 */
@Data
public class DeviceConfigInfo {

    private String id;

    private String deviceCode;

    /**
     * 产品key
     */
    private String prodKey;

    private String deviceName;

    /**
     * 设备配置json内容
     */
    private String config;
}
