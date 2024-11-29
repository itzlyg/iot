package cn.sinozg.applet.biz.notify.enums;

import cn.sinozg.applet.biz.notify.model.channel.NotifierConfigBaseInfo;
import cn.sinozg.applet.biz.notify.model.channel.NotifierDingTalkConfig;
import cn.sinozg.applet.biz.notify.model.channel.NotifierEmailConfig;
import cn.sinozg.applet.biz.notify.model.channel.NotifierFeiShuConfig;
import cn.sinozg.applet.biz.notify.model.channel.NotifierSmsConfig;
import cn.sinozg.applet.biz.notify.model.channel.NotifierWebHookConfig;
import cn.sinozg.applet.biz.notify.model.channel.NotifierWwAppConfig;
import cn.sinozg.applet.biz.notify.model.channel.NotifierWwRobotConfig;
import lombok.Getter;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-12 20:55
 */
@Getter
public enum NotifierType {
    /** 短信 */
    SMS("01", "短信", NotifierSmsConfig.class),
    /** 邮件 */
    EMAIL("02", "邮件", NotifierEmailConfig.class),
    /** 钉钉 */
    DING_TALK("03", "钉钉", null),
    DING_TALK_ROBOT("04", "钉钉机器人", NotifierDingTalkConfig.class),
    /** 微信 */
    WE_CHAT("05", "微信", null),
    /** 微信 */
    WE_CHAT_OFFICIAL("06", "微信", null),
    /** 飞书 */
    FEI_SHU("07", "飞书webHook", NotifierFeiShuConfig.class),
    /** 企微机器人 */
    WE_WORK_ROBOT("08", "企微机器人", NotifierWwRobotConfig.class),
    /** 企微app */
    WE_WORK_APP("09", "企微app", NotifierWwAppConfig.class),
    /** webHook */
    WEB_HOOK("10", "webHook", NotifierWebHookConfig.class),
    ;

    private final String code;

    private final String name;

    private final Class<? extends NotifierConfigBaseInfo> infoClass;

    NotifierType(String code, String name, Class<? extends NotifierConfigBaseInfo> infoClass){
        this.code = code;
        this.name = name;
        this.infoClass = infoClass;
    }

    public static NotifierType ofCode (String code){
        for (NotifierType value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
