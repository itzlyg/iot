package cn.sinozg.applet.biz.protocol.enums;

import cn.sinozg.applet.biz.protocol.vo.module.BaseProtocolConfig;
import cn.sinozg.applet.biz.protocol.vo.module.ProtocolConfigHttp;
import cn.sinozg.applet.biz.protocol.vo.module.ProtocolConfigMqtt;
import cn.sinozg.applet.biz.protocol.vo.module.ProtocolConfigTcp;
import cn.sinozg.applet.biz.protocol.vo.module.ProtocolConfigWebSocket;
import cn.sinozg.applet.biz.protocol.vo.valid.ProtocolModuleConfigValidation;
import cn.sinozg.applet.common.exception.CavException;
import lombok.Getter;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-22 12:18
 */
@Getter
public enum ProtocolType {

    /** MQTT */
    MQTT("01", "MQTT", "mqttConfig", ProtocolConfigMqtt.class, ProtocolModuleConfigValidation.Mqtt.class),
    /** HTTP */
    HTTP("02", "HTTP", "httpConfig", ProtocolConfigHttp.class, ProtocolModuleConfigValidation.Http.class),
    /** TCP */
    TCP("03", "TCP", "tcpConfig", ProtocolConfigTcp.class, ProtocolModuleConfigValidation.Tcp.class),
    /** WebSocket */
    WEBSOCKET("04", "WebSocket", "webConfig", ProtocolConfigWebSocket.class, ProtocolModuleConfigValidation.WebSocket.class),
    ;

    private final String code;

    private final String name;

    private final String fieldName;

    private final Class<? extends BaseProtocolConfig> configBean;

    private final Class<?> validBean;

    ProtocolType(String code, String name, String fieldName, Class<? extends BaseProtocolConfig> configBean, Class<?> validBean){
        this.code = code;
        this.name = name;
        this.fieldName = fieldName;
        this.configBean = configBean;
        this.validBean = validBean;
    }

    public static ProtocolType ofCode(String code){
        for (ProtocolType value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        throw new CavException("未定义的通讯协议类型");
    }
}
