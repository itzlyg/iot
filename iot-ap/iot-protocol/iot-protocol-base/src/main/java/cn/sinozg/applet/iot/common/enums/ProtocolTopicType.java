package cn.sinozg.applet.iot.common.enums;

import cn.sinozg.applet.mq.joint.BaseTopicType;
import lombok.Getter;

/**
 * 协议相关的topic 用于消息转发
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-18 15:27
 */
@Getter
public enum ProtocolTopicType implements BaseTopicType {
    /** 物模型 */
    THING_MODEL ("device_thing", "物模型"),
    /** 设备属性 */
    DEVICE_PROPERTY ("device_property", "设备属性"),
    /** 设备上报记录 用于统计 */
    DEVICE_REPORT ("device_report", "设备上报记录"),
    /** 设备配置  */
    DEVICE_CONFIG ("device_config", "设备配置"),
    /** 设备配置  */
    DEVICE_HTTP ("device_http", "http消费设备信息"),
    /** 设备信息  */
    DEVICE_INFO ("device_info:", "设备信息")
    ;
    private final String code;

    private final String name;

    ProtocolTopicType(String code, String name){
        this.code = code;
        this.name = name;
    }

    @Override
    public String topic() {
        return code;
    }
}
