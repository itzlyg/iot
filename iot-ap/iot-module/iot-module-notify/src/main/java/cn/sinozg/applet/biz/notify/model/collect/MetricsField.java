package cn.sinozg.applet.biz.notify.model.collect;

import lombok.Data;

import java.util.Map;

/**
 * @Description 
 * @Copyright Copyright (c) 2024
 * @author xieyubin
 * @since 2024-01-22 12:41:52
 */
@Data
public class MetricsField {

    /**
     * Metric name
     * 指标名称
     */
    private String field;
    /**
     * metric field name's i18n value
     * 指标的国际化名称
     * zh-CN: CPU 版本号
     * en-US: CPU Version
     */
    private Map<String, String> i18n;
    /**
     * Metric type 0-number: number 1-string: string
     * 指标类型 0-number:数字 1-string:字符串
     */
    private byte type = 1;
    /**
     * Whether this field is the instance
     */
    private boolean instance = false;
    /**
     * Whether this field is the label
     */
    private boolean label = false;
    /**
     * Metric unit
     */
    private String unit;
    
}
