package cn.sinozg.applet.biz.sink.entity;

import cn.sinozg.applet.common.core.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 产品属性定义表表
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-03-07 15:35:53
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sink_property_defin")
@Schema(name = "PropertyDefin", description = "产品属性定义表")
public class PropertyDefin extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /** 产品key */
    @Schema(description = "产品key")
    @TableField("prod_key")
    private String prodKey;


    /** 属性标识 即key */
    @Schema(description = "属性标识 即key")
    @TableField("identifier")
    private String identifier;


    /** 数据类型 */
    @Schema(description = "数据类型")
    @TableField("type")
    private String type;


    /** 数据类型规格，比如长度等、、 */
    @Schema(description = "数据类型规格，比如长度等、、")
    @TableField("specs")
    private String specs;
}
