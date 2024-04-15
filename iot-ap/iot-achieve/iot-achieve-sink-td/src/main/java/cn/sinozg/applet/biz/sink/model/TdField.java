package cn.sinozg.applet.biz.sink.model;

import cn.sinozg.applet.biz.sink.common.enums.DbDataType;
import lombok.Data;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-06 14:03
 */
@Data
public class TdField {
    private String name;
    private DbDataType type;
    private int length;

    public TdField(String name, DbDataType type, int length){
        this.name = name;
        this.type = type;
        this.length = length;
    }

    public static TdField string(String name){
        return new TdField(name, DbDataType.STRING, 50);
    }
    public static TdField string(String name, int length){
        return new TdField(name, DbDataType.STRING, length);
    }

    public static TdField other(String name, DbDataType type){
        return new TdField(name, type, -1);
    }
}
