package cn.sinozg.applet.biz.notify.enums;

import lombok.Getter;

/**
 * 数据空间大小的枚举类
 * @Description
 * @Copyright Copyright (c) 2024
 * @author xieyubin
 * @since 2024-01-26 16:00:44
 */
@Getter
public enum DataUnitType {
    /**
     * byte
     */
    B("B", 1),
    /**
     * kilobyte
     */
    KB("KB", 1024),
    /**
     * kilobyte
     */
    K("K", 1024),
    /**
     * kilobyte
     */
    KI("KI", 1024),
    /**
     * megabyte
     * 1024 * 1024
     */
    MB("MB", 1_048_576),
    /**
     * megabyte
     * 1024 * 1024
     */
    MI("MI", 1_048_576),
    /**
     * megabyte
     * 1024 * 1024
     */
    M("M", 1_048_576),
    /**
     * gigabyte
     * 1024 * 1024 * 1024
     */
    GB("GB", 1_073_741_824),
    /**
     * gigabyte
     * 1024 * 1024 * 1024
     */
    GI("GI", 1_073_741_824),
    /**
     * gigabyte
     * 1024 * 1024 * 1024
     */
    G("G", 1_073_741_824);

    private final String unit;
    private final long scale;

    DataUnitType(String unit, long scale) {
        this.unit = unit;
        this.scale = scale;
    }
}
