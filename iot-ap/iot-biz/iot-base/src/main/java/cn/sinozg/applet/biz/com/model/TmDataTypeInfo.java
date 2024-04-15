package cn.sinozg.applet.biz.com.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 数据具体消息
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-07 22:19
 */
@Data
public class TmDataTypeInfo {

    @Schema(description = "数据类型")
    private String type;

    @Schema(description = "数据类型规格，比如长度等、、")
    private Object specs;
}
