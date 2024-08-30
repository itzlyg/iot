package cn.sinozg.applet.instruct.util;

import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.instruct.annotate.InstructField;
import cn.sinozg.applet.instruct.model.InstructFieldDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 指令任务
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-03-13 14:44
 */
@Slf4j
public class InstructUtil {
    private static final int TWO = 2;

    private static final Map<Class<?>, List<InstructFieldDetail>> STRUCTURE_MAP = new ConcurrentHashMap<>();


    /**
     * 解码指令
     * @param instruct 指令
     * @param t 指令实体对象
     * @param <T> 指令实体对象类型
     */
    public static <T> void decodeInstruct(String instruct, T t) {
        List<InstructFieldDetail> structureList = structureInfo(t.getClass());
        try {
            itemDecodeField(structureList, t, instruct);
        } catch (Exception e) {
            log.error("解码错误，，，", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 编码指令
     * @param t 对象
     * @return 指令
     * @param <T> 对象类型
     */
    public static <T> String encodedInstruct(T t) {
        List<InstructFieldDetail> structureList = structureInfo(t.getClass());
        StringBuilder instruct = new StringBuilder();
        try {
            itemEncodeField(structureList, t, instruct);
        } catch (Exception e) {
            log.error("编码错误，，，", e);
            throw new RuntimeException(e);
        }
        return instruct.toString();
    }


    /**
     * 循环解码
     * @param list 结构
     * @param o 数据
     * @param instruct 指令
     * @throws Exception 异常
     */
    private static void itemDecodeField (List<InstructFieldDetail> list, Object o, String instruct) throws Exception {
        Field field;
        for (InstructFieldDetail d : list) {
            field = d.getField();
            field.setAccessible(true);
            Object v;
            if (CollectionUtils.isNotEmpty(d.getDetails())) {
                v = PojoUtil.newInstance(d.getType());
                itemDecodeField(d.getDetails(), v, instruct);
            } else {
                if (d.getBs() == 0) {
                    v = StringUtils.EMPTY;
                } else {
                    v = decodeField(instruct, field, d);
                }
            }
            field.set(o, v);
        }
    }

    /**
     * 循环编码
     * @param list 结构体
     * @param o 对象
     * @param instruct 指令
     * @throws Exception 异常
     */
    private static void itemEncodeField(List<InstructFieldDetail> list, Object o, StringBuilder instruct) throws Exception {
        Field field;
        Object value;
        String v;
        for (InstructFieldDetail d : list) {
            field = d.getField();
            field.setAccessible(true);
            value = field.get(o);
            if (CollectionUtils.isNotEmpty(d.getDetails())) {
                List<Object> subs = PojoUtil.cast(value);
                for (Object sub : subs) {
                    itemEncodeField(d.getDetails(), sub, instruct);
                }
            } else {
                v = encodeField(field, value, d);
                instruct.append(v);
            }
        }
    }

    /**
     * 编码字段
     * @param instruct 指令
     * @param field 字段
     * @param d 字段信息
     * @return 值
     */
    private static Object decodeField (String instruct, Field field, InstructFieldDetail d){
        String s = StringUtils.substring(instruct, d.getBegin(), d.getBegin() + TWO * d.getBs());
        if (d.isRevert()) {
            s = CodecUtil.revertStrLen2(s);
        }
        Object v = s;
        // 时间
        if (field.getType() == LocalDateTime.class) {
            v = CodecUtil.ldtTime(s, d.getPattern());
        }
        // 转10 进制
        if (d.isDecimalHex()) {
            v = CodecUtil.hexToInt(s);
        }
        // ascii to string
        if (d.isAscii()) {
            v = CodecUtil.asciiToString(s);
        }
        // 16 进制转 10 进制 浮点
        if (d.isFloatHex()) {
            float f = CodecUtil.hexToFloat(s);
            v = new BigDecimal(f);
        }
        // 小数和整数 转换系数
        if (d.getRatio() != null) {
            BigDecimal bg = new BigDecimal(String.valueOf(v));
            BigDecimal ratio = BigDecimal.valueOf(Math.pow(10, d.getRatio()));
            v = bg.multiply(ratio);
        }
        // 保留位数
        if (d.getScale() != null) {
            BigDecimal bg = new BigDecimal(String.valueOf(v));
            v = bg.setScale(d.getScale(), RoundingMode.HALF_UP);
        }
        // 16 进制转 2 进制
        if (d.isHexToBinary()) {
            v = CodecUtil.hexToBinary(s, d.getBs());
        }
        // 先反转 十六进制转二进制再截取后转十进制
        int[] hexToBinInt = d.getHexToBinInt();
        if (ArrayUtils.isNotEmpty(hexToBinInt)) {
            String bin = CodecUtil.hexToBinary(s, d.getBs());
            Integer[] binInt = new Integer[hexToBinInt.length];
            int index = bin.length();
            for (int i = 0; i < hexToBinInt.length; i++) {
                binInt[i] = CodecUtil.binaryToInt(bin.substring(index - hexToBinInt[i], index));
                index -= hexToBinInt[i];
            }
            v = binInt;
        }
        return v;
    }

    /**
     * 编码指令
     * @param field 字段
     * @param data 数据
     * @param d 对应的结构
     * @return 指令
     */
    private static String encodeField (Field field, Object data, InstructFieldDetail d) {
        String fv = null;
        if (field.getType() == LocalDateTime.class) {
            fv = CodecUtil.ldtTime((LocalDateTime) data, d.getPattern());
        }
        // 转10 进制
        if (d.isDecimalHex()) {
            assert data instanceof Integer;
            fv = CodecUtil.intToHex((Integer) data, d.getBs());
        }
        // ascii to string
        if (d.isAscii()) {
            fv = CodecUtil.stringToAscii(String.valueOf(data));
        }
        // 16 进制转 2 进制
        if (d.isHexToBinary()) {
            assert data instanceof Byte;
            fv = CodecUtil.byteToHex((Byte) data, d.getBs());
        }
        if (fv == null) {
            fv = CodecUtil.leftPad(String.valueOf(data), d.getBs());
        }

        return fv;
    }

    /**
     * 获取到指令结构
     * @param clazz 数据类型
     * @return 结构体
     */
    public static List<InstructFieldDetail> structureInfo(Class<?> clazz) {
        if (!STRUCTURE_MAP.containsKey(clazz)) {
            Field[] fields = clazz.getDeclaredFields();
            List<InstructFieldDetail> list = new ArrayList<>();
            InstructField insField;
            Field field;
            InstructFieldDetail detail;
            int begin = 0;
            for (int i = 0, j = fields.length; i < j; i++) {
                field = fields[i];
                insField = field.getAnnotation(InstructField.class);
                if (insField != null) {
                    detail = new InstructFieldDetail(field, i, begin, insField);
                    begin = detail.getBegin() + detail.getBs() * TWO;
                    list.add(detail);
                }
            }
            list.sort(Comparator.comparingInt(InstructFieldDetail::getIndex));
            STRUCTURE_MAP.put(clazz, list);
        }
        return STRUCTURE_MAP.get(clazz);
    }
}
