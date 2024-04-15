package cn.sinozg.applet.biz.device.service;

import cn.sinozg.applet.biz.com.enums.TmIdentifierType;

import java.util.List;
import java.util.Map;

/**
* 设备发送指令
* @Description
* @Copyright Copyright (c) 2023
* @author wang.pf
* @since 2023-12-03 16:27:01
*/
public interface DeviceInfoSendService {

    /**
     * 调用设备服务
     * @param deviceId 设备id
     * @param sendName sendName 冗余参数 做搜索用
     * @param identifier 类型
     * @see TmIdentifierType
     * @param args 参数
     * @return 结果
     */
    String invokeService(String deviceId, String sendName, String identifier, Map<String, Object> args);

    /**
     * ota升级
     * @param deviceId 设备id
     *
     * @param data 参数
     * @return 结果
     */
    String otaUpgrade(String deviceId, String sendName, Object data);

    /**
     * 获取属性
     * @param deviceId 设备id
     * @param sendName sendName
     * @param properties 参数
     * @return 结果
     */
    String getProperty(String deviceId, String sendName, List<String> properties);

    /**
     * 设置属性
     * @param deviceId 设备id
     * @param sendName sendName
     * @param properties 参数配置
     * @return 结果
     */
    String setProperty(String deviceId, String sendName, Map<String, Object> properties);

    /**
     * 属性上报
     * @param deviceId 设备id
     * @param sendName sendName
     * @param properties 参数配置
     * @return 结果
     */
    String reportProperty(String deviceId, String sendName, Map<String, Object> properties);

    /**
     * 设置配置
     * @param deviceId 设备id
     * @param sendName sendName
     * @return 结果
     */
    String sendConfig(String deviceId, String sendName);

    /**
     * 解绑子设备
     * @param deviceId 设备id
     * @param sendName sendName
     */
    void unbindDevice(String deviceId, String sendName);

    /**
     * 发送任务
     * @param deviceId 设备id
     * @param sendName sendName
     * @param o 数据
     */
    void sendTask (String deviceId, String sendName, Object o);

    /**
     * 发送 ack
     * @param deviceId 设备id
     * @param o 数据
     */
    void sendAck (String deviceId, Object o);

}
