package cn.sinozg.applet.biz.product.vo.request;

import cn.sinozg.applet.biz.product.vo.request.table.ProdInfoJsonList;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
* 产品消息表表 新增请求参数
* @Description
* @Copyright Copyright (c) 2023
* @author zy
* @since 2023-11-27 21:44:29
*/
@Getter
@Setter
@Schema(name = "ProdInfoCreateRequest", description = "产品消息表 新增请求参数")
public class ProdInfoCreateRequest extends ProdInfoJsonList {

    /** 名称：唯一标识物体的名称 */
    @Schema(description = "名称：唯一标识物体的名称")
    private String name;

    /** 图片ID：存储图片的ID字符串 */
    @Schema(description = "图片ID：存储图片的ID字符串")
    private String imgId;

    /** 分类ID：用于对物体进行分类 */
    @Schema(description = "分类ID：用于对物体进行分类")
    private String classifyId;

    /** 协议ID：用于关联协议相关信息 */
    @Schema(description = "协议ID：用于关联协议相关信息")
    private String protocolId;

    /** 数据类型枚举：表示物体的类型 */
    @Schema(description = "数据类型枚举：表示物体的类型")
    private String typeId;

    /** 数据类型名称：与type_id一一对应，用于描述物体类型 */
    @Schema(description = "数据类型名称：与type_id一一对应，用于描述物体类型")
    private String typeName;
}
