package cn.sinozg.applet.biz.notify.enums;

import cn.sinozg.applet.common.exception.CavException;
import lombok.Getter;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-29 18:05
 */
@Getter
public enum ConditionType {

    /** 大于 */
    GT ("01", "大于"),
    LT ("02", "小于"),
    EQ("03", "等于"),
    GE("04", "大于等于"),
    LE("05", "小于等于"),
    NE("06", "不等于"),
    EXISTS("07", "存在"),
    EXISTS_NON("08", "不存在"),
    MATCHES("09", "匹配"),
    MATCHES_NON("10", "不匹配"),
    CONTAINS("11", "包含"),
    CONTAINS_NON("12", "不包含"),
    ;

    private final String code;
    private final String name;

    ConditionType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ConditionType ofCode (String code){
        for (ConditionType value : values()) {
            if (code.equals(value.getCode())) {
                return value;
            }
        }
        throw new CavException("无效的运算符号！");
    }
}
