package cn.sinozg.applet.biz.notify.collect;

/**
 * the interface of unit convert
 * 单位转换的接口类，处理 MetricsInfo#units
 * @Description
 * @Copyright Copyright (c) 2024
 * @author xieyubin
 * @since 2024-01-22 18:41:25
 */
public interface UnitConvert {

    /**
     * convert originUnit value to newUnit value
     * 将当前originUnit对应的value转换为newUnit对应的value
     * @param value 收集到的值
     * @param originUnit 原值对应的单位
     * @param newUnit 展示的单位
     * @return 转换后的value
     */
    String convert(String value, String originUnit, String newUnit);

    /**
     * check the unit and confirm to use this implement class
     * 检查 xxx -> xxx前后两个单位，也为了确认是否使用该实现类
     * @param unit 单位
     * @return true/false
     */
    boolean checkUnit(String unit);
}
