package cn.sinozg.applet.turbo.engine.common;

/**
 * 数据类型
 * @Description
 * @Copyright Copyright (c) 2024
 * @author xieyubin
 * @since 2024-03-23 15:27:43
 */
public enum DataType {
    /** 数据类型 */
    T_STRING(1, "string"),
    T_INTEGER(2, "integer"),
    T_LONG(3, "long"),
    T_DOUBLE(4, "double"),
    T_LIST(5, "list");

    private int type;
    private String name;

    private DataType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    private void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public static DataType getType(String name) {
        DataType[] dataTypes = values();
        for (DataType dataType : dataTypes) {
            if (dataType.getName().equals(name)) {
                return dataType;
            }
        }
        return null;
    }
}
