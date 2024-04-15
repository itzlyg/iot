package cn.sinozg.applet.biz.product.vo.request.table;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-14 14:26
 */
@Getter
@Setter
public class ProdInfoJsonList {
    /** 属性JSON数据：存储与物体相关的属性信息 */
    @Valid
    @Schema(description = "属性JSON数据：存储与物体相关的属性信息")
    private List<AttributeTable> attributeJson;

    /** 能JSON数据：存储与物体相关的功能信息 */
    @Valid
    @Schema(description = "能JSON数据：存储与物体相关的功能信息")
    private List<FunctionTable> functionJson;

    /** 事件JSON数据：存储与物体相关的事件信息 */
    @Valid
    @Schema(description = "事件JSON数据：存储与物体相关的事件信息")
    private List<EventTable> eventJson;
}
