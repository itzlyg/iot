package cn.sinozg.applet.biz.notify.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-29 17:25
 */
@Data
public class NoticeConditionInfo {

    /** 定义id */
    @Schema(description = "定义id")
    private String defineId;

    /** 字段 */
    @Schema(description = "字段")
    private String field;

    /** 字段类型 */
    @Schema(description = "字段类型 01:数字 02:字符串 03:boolean")
    private String fieldType;

    /** 条件 */
    @Schema(description = "条件")
    private String condition;

    /** 对比值 */
    @Schema(description = "对比值")
    private String compValue;
}
