package cn.sinozg.applet.biz.product.entity;

import cn.sinozg.applet.common.core.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 产品消息表表
* @Description
* @Copyright Copyright (c) 2023
* @author zy
* @since 2023-11-27 21:44:29
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_set_prod_info")
@Schema(name = "ProdInfo", description = "产品消息表")
public class ProdInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /** 产品key */
    @Schema(description = "产品key")
    @TableField(value = "prod_key", updateStrategy = FieldStrategy.NEVER)
    private String prodKey;

    /** 名称：唯一标识物体的名称 */
    @Schema(description = "名称：唯一标识物体的名称")
    @TableField("name")
    private String name;


    /** 图片ID：存储图片的ID字符串 */
    @Schema(description = "图片ID：存储图片的ID字符串")
    @TableField("img_id")
    private String imgId;


    /** 分类ID：用于对物体进行分类 */
    @Schema(description = "分类ID：用于对物体进行分类")
    @TableField("classify_id")
    private String classifyId;


    /** 协议ID：用于关联协议相关信息 */
    @Schema(description = "协议ID：用于关联协议相关信息")
    @TableField("protocol_id")
    private String protocolId;


    /** 数据类型枚举：表示物体的类型 */
    @Schema(description = "数据类型枚举：表示物体的类型")
    @TableField("type_id")
    private String typeId;


    /** 数据类型名称：与type_id一一对应，用于描述物体类型 */
    @Schema(description = "数据类型名称：与type_id一一对应，用于描述物体类型")
    @TableField("type_name")
    private String typeName;


    /** 属性JSON数据：存储与物体相关的属性信息 */
    @Schema(description = "属性JSON数据：存储与物体相关的属性信息")
    @TableField("attribute_json")
    private String attributeJson;


    /** 能JSON数据：存储与物体相关的功能信息 */
    @Schema(description = "能JSON数据：存储与物体相关的功能信息")
    @TableField("function_json")
    private String functionJson;


    /** 事件JSON数据：存储与物体相关的事件信息 */
    @Schema(description = "事件JSON数据：存储与物体相关的事件信息")
    @TableField("event_json")
    private String eventJson;


    /** 租户id */
    @Schema(description = "租户id")
    @TableField(value = "tenant_id", updateStrategy = FieldStrategy.NEVER)
    private String tenantId;

}
