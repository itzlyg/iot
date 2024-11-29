package cn.sinozg.applet.biz.notify.enums;

import lombok.Getter;

/**
 * @Description 
 * @Copyright Copyright (c) 2024
 * @author xieyubin
 * @since 2024-01-26 16:04:37
 */
@Getter
public enum TimeLengthUnit {
    /**
     * NANOSECONDS
     */
    NS("NS", 1),
    /**
     * MICROSECONDS
     */
    US("US", 1000),
    /**
     * MILLISECONDS
     */
    MS("MS", 1000_000),
    /**
     * SECONDS
     */
    S("S", 1000_000_000),
    /**
     * MINUTES
     */
    MIN("MIN", 60_000_000_000L),
    /**
     * HOURS
     */
    H("H", 3600_000_000_000L),
    /**
     * DAYS
     */
    D("D", 86_400_000_000_000L);

    private final String unit;
    private final long scale;

    TimeLengthUnit(String unit, long scale) {
        this.unit = unit;
        this.scale = scale;
    }
}
