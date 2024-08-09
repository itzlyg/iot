package cn.sinozg.applet.instruct.annotate;

import cn.sinozg.applet.common.utils.DateUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-08-09 21:03
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InstructField {
    /** 开始位置 默认-1 不需要指定 */
    int begin() default -1;
    /** 字节位数 -1 表示 未知*/
    int bs() default -1;
    /** 到设备是十六进制，显示是十进制 */
    boolean decimalHex() default false;
    /** 到设备是十六进制，显示是十进制 浮点数 BigDecimal 接收 */
    boolean floatHex() default false;
    /** 转小数或者整数时候的系数，10的幂次 BigDecimal 接收 */
    int ratio() default 0;
    /** 小数点保留位数 BigDecimal 接收 */
    int scale() default Integer.MAX_VALUE;
    /** 字符串于 ascii 互转 */
    boolean ascii() default false;
    /** 将字符串 按照字节反转 */
    boolean revert() default false;
    /** 十六进制转二进制 */
    boolean hexToBinary() default false;
    /** 十六进制转二进制再截取后转十进制 */
    int[] hexToBinInt() default {};
    /** 字符串与日期之间的转化 */
    String pattern() default DateUtil.HHMMSS;
    /** 排序 */
    int sort() default Integer.MAX_VALUE;
}
