package cn.sinozg.applet.biz.notify.collect.impl;

import cn.sinozg.applet.biz.notify.collect.UnitConvert;
import cn.sinozg.applet.biz.notify.enums.TimeLengthUnit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 时间长短转换
 * @Description
 * @Copyright Copyright (c) 2024
 * @author xieyubin
 * @since 2024-01-26 16:03:47
 */
@Component
public final class TimeLengthConvert implements UnitConvert {

    @Override
    public String convert(String value, String originUnit, String newUnit) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        BigDecimal length = new BigDecimal(value);
        // 思路：value通过originUnit转换为纳秒，在转换为newUnit单位对应的值
        for (TimeLengthUnit unit : TimeLengthUnit.values()) {
            if (StringUtils.equalsIgnoreCase(unit.getUnit(), originUnit)) {
                length = length.multiply(new BigDecimal(unit.getScale()));
            }
            if (StringUtils.equalsIgnoreCase(unit.getUnit(), newUnit)) {
                length = length.divide(new BigDecimal(unit.getScale()), 12, RoundingMode.HALF_UP);
            }
        }
        return length.setScale(4, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
    }

    @Override
    public boolean checkUnit(String unit) {
        if (StringUtils.isBlank(unit)) {
            return false;
        }
        for (TimeLengthUnit timeUnit : TimeLengthUnit.values()) {
            // 不区分大小写
            if (timeUnit.getUnit().equals(unit.toUpperCase())) {
                return true;
            }
        }
        return false;
    }
}
