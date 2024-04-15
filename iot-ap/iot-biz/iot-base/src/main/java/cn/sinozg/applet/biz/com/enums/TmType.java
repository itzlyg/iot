package cn.sinozg.applet.biz.com.enums;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 物模型类型
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-25 13:07
 */
@Getter
public enum TmType {


    /** 生命周期 */
    LIFE_TIME ("01", "lifetime", "生命周期"),
    /** 状态 */
    STATE ("02", "state", "状态"),
    /** 属性 */
    PROPERTY ("03", "property", "属性"),
    /** 事件 */
    EVENT ("04", "event", "事件"),
    /** 服务 */
    SERVICE ("05", "service", "服务"),

    /** ota */
    OTA ("06", "ota", "ota"),
    /** 配置 */
    CONFIG ("07", "config", "配置"),
    ;

    private final String code;

    private final String name;

    private final String enName;

    TmType(String code, String enName, String name){
        this.code = code;
        this.enName = enName;
        this.name = name;
    }

    public static boolean eq (String enName, TmType tp){
        return StringUtils.equals(tp.getEnName(), enName);
    }

    public static TmType ofEnName (String enName){
        for (TmType value : values()) {
            if (value.getEnName().equals(enName)) {
                return value;
            }
        }
        return null;
    }
}
