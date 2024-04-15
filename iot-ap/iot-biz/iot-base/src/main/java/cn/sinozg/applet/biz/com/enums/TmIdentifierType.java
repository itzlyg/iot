package cn.sinozg.applet.biz.com.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 物模型 标志符
 * 对应设备的具体事件
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-25 13:07
 */
@Getter
public enum TmIdentifierType {


    /** 生命周期 */
    GET ("01", "get", "GET"),
    /** 状态 */
    SET ("02", "set", "SET"),
    /** 上报 */
    REPORT("03", "report", "上报"),
    /** 在线 */
    ONLINE("04", "online", "在线"),
    /** 离线 */
    OFFLINE("05", "offline", "离线"),
    /** 注册 */
    REGISTER("06", "register", "注册"),
    /** 注销 */
    DEREGISTER ("07", "deregister", "注销"),
    /** OTA */
    OTA ("08", "ota", "OTA"),
    /** ack */
    ACK ("09", "ack", "ack"),
    /** 答复 */
    REPLY ("99", "_reply", "答复"),

    ;
    private final String code;

    private final String name;

    private final String enName;

    TmIdentifierType(String code, String enName, String name){
        this.code = code;
        this.enName = enName;
        this.name = name;
    }

    public static boolean isReply(String enName){
        return StringUtils.endsWith(enName, REPLY.getEnName());
    }

    public static String reply(TmIdentifierType ide){
        return ide.getEnName() + REPLY.getEnName();
    }

    public static boolean eq (String enName, TmIdentifierType enums){
        return StringUtils.equals(enName, enums.getEnName());
    }
    public static TmIdentifierType ofEnName (String enName){
        for (TmIdentifierType value : values()) {
            if (value.getEnName().equals(enName)) {
                return value;
            }
        }
        return null;
    }
}
