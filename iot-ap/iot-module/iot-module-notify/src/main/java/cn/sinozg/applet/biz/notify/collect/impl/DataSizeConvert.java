package cn.sinozg.applet.biz.notify.collect.impl;

import cn.sinozg.applet.biz.notify.collect.UnitConvert;
import cn.sinozg.applet.biz.notify.enums.DataUnitType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * the convert of data size
 * @Description
 * @Copyright Copyright (c) 2024
 * @author xyb
 * @since 2024-10-23 19:59:23
*/
@Component
public final class DataSizeConvert implements UnitConvert {

    @Override
    public String convert(String value, String originUnit, String newUnit) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        BigDecimal size = new BigDecimal(value);
        // 思路：value通过originUnit转换为字节，在转换为newUnit单位对应的值
        for (DataUnitType dataUnit : DataUnitType.values()) {
            if (StringUtils.equalsIgnoreCase(dataUnit.getUnit(), originUnit)) {
                size = size.multiply(new BigDecimal(dataUnit.getScale()));
            }
            if (StringUtils.equalsIgnoreCase(dataUnit.getUnit(), newUnit)) {
                size = size.divide(new BigDecimal(dataUnit.getScale()), 12, RoundingMode.HALF_UP);
            }
        }
        return size.setScale(4, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
    }

    @Override
    public boolean checkUnit(String unit) {
        if (StringUtils.isBlank(unit)) {
            return false;
        }
        for (DataUnitType dataUnit : DataUnitType.values()) {
            // 不区分大小写
            if (dataUnit.getUnit().equals(unit.toUpperCase())) {
                return true;
            }
        }
        return false;
    }
}
