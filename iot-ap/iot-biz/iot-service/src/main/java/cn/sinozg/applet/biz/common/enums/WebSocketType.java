package cn.sinozg.applet.biz.common.enums;

import cn.sinozg.applet.ws.joint.WsBaseEnum;
import lombok.Getter;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-18 20:22
 */
@Getter
public enum WebSocketType implements WsBaseEnum {
    /** 设备 */
    DEVICE("device", "设备"),

    DEVICE_PROPERTIES("device_attribute", "设备属性"),
    ;
    /** code */
    private final String code;
    /** name */
    private final String name;
            ;
    WebSocketType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String code() {
        return code;
    }
}
