package cn.sinozg.applet.iot.common.enums;

import lombok.Getter;

/**
 * 消息发送类型类型
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-15 16:44
 */
@Getter
public enum ProtoMessageType {

    /** 授权 */
    AUTH ("auth", "授权认证"),
    /** 链接 */
    CONNECT ("connect", "链接"),
    CONNECTED ("connected", "链接"),
    /** 断开链接 */
    DISCONNECT ("disconnect", "断开链接"),
    /** 订阅 */
    SUBSCRIBE("subscribe", "订阅"),
    /** 取消订阅 */
    UNSUBSCRIBE("unsubscribe", "取消订阅"),
    /** ota */
    OTA("ota", "ota"),
    PING("ping", "ping"),
    /** 空 */
    EMPTY("", "空"),
    ;

    private final String code;

    private final String name;
    ProtoMessageType(String code, String name){
        this.code = code;
        this.name = name;
    }

}
