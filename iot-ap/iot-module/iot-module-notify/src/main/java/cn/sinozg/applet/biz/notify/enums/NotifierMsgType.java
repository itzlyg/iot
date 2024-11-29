package cn.sinozg.applet.biz.notify.enums;

import lombok.Getter;

/**
 * 通知消息类型
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-30 20:55
 */
@Getter
public enum NotifierMsgType {
    /** 文本 */
    TEXT("01", "文本"),

    MARKDOWN("02", "markdown"),
    ;

    private final String code;

    private final String name;

    NotifierMsgType(String code, String name){
        this.code = code;
        this.name = name;
    }
}
