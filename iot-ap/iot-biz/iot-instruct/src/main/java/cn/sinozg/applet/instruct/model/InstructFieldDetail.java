package cn.sinozg.applet.instruct.model;

import cn.sinozg.applet.instruct.annotate.InstructField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author xyb
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-08-09 13:34
 */
@Data
public class InstructFieldDetail {

    private Field field;

    @Schema(description = "十六进制转十进制")
    private boolean decimalHex;

    @Schema(description = "十六进制转十进制 浮点数")
    private boolean floatHex;

    @Schema(description = "系数指数")
    private Integer ratio;

    @Schema(description = "小数 保留位数")
    private Integer scale;

    @Schema(description = "十六进制转二进制")
    private boolean hexToBinary;

    @Schema(description = "ascii转字符串")
    private boolean ascii;

    @Schema(description = "反转")
    private boolean revert;

    @Schema(description = "下标")
    private int index;

    @Schema(description = "开始下标")
    private int begin;

    @Schema(description = "字节数")
    private int bs;

    @Schema(description = "十六进制转二进制再截取后转十进制")
    private int[] hexToBinInt;

    @Schema(description = "是否数组")
    private boolean isArray;

    @Schema(description = "子数据")
    private List<InstructFieldDetail> details;

    @Schema(description = "子协议的时候的对象类型")
    private Class<?> type;

    @Schema(description = "时间格式")
    private String pattern;

    public InstructFieldDetail(Field field, int index, int begin, InstructField info){
        this.field = field;
        this.index = index;
        if (info.sort() != Integer.MAX_VALUE) {
            this.index = info.sort();
        }
        this.begin = info.begin() == -1 ? begin : info.begin();
        this.bs = info.bs();
        this.floatHex = info.floatHex();
        this.ratio = info.ratio() == 0 ? null : info.ratio();
        this.scale = info.scale() == Integer.MAX_VALUE ? null : info.scale();
        this.hexToBinary = info.hexToBinary();
        this.decimalHex = info.decimalHex();
        this.hexToBinInt = info.hexToBinInt();
        this.revert = info.revert();
        this.ascii = info.ascii();
        this.pattern = info.pattern();
    }

    public boolean isUnknown() {
        return bs == -1;
    }

}
