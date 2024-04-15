package cn.sinozg.applet.biz.sink.common.enums;

import lombok.Getter;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-06 14:12
 */
@Getter
public enum DbDataType {

    /** int */
    INT ("01", "INT"),
    /** long */
    LONG ("02", "BIGINT"),
    /** FLOAT */
    FLOAT ("03", "FLOAT"),
    /** double */
    DOUBLE ("04", "DOUBLE"),
    /** string */
    STRING ("05", "NCHAR"),
    /** boolean */
    BOOLEAN ("06", "BOOL"),
    /** DATE */
    DATE ("07", "NCHAR"),
    /** ENUM */
    ENUM ("09", "TINYINT"),
    /** TIMESTAMP */
    TIMESTAMP ("99", "TIMESTAMP"),
    ;

    private final String code;

    private final String tdType;

    DbDataType(String code, String tdType){
        this.code = code;
        this.tdType = tdType;
    }

    public static DbDataType ofCode (String code){
        for (DbDataType v : values()) {
            if (v.getCode().equals(code)) {
                return v;
            }
        }
        return null;
    }
    public static DbDataType ofType (String type){
        for (DbDataType v : values()) {
            if (v.getTdType().equals(type)) {
                return v;
            }
        }
        return null;
    }


}
