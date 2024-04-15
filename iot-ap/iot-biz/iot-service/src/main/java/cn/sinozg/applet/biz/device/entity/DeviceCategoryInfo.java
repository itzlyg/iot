package cn.sinozg.applet.biz.device.entity;

import cn.sinozg.applet.common.core.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 设备分类信息表表
* @Description
* @Copyright Copyright (c) 2023
* @author wang.pf
* @since 2023-12-03 16:48:09
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_set_device_category_info")
@Schema(name = "DeviceCategoryInfo", description = "设备分类信息表")
public class DeviceCategoryInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /** 产品分类代码 */
    @Schema(description = "产品分类代码")
    @TableField("category_code")
    private String categoryCode;


    /** 父级产品分类代码 */
    @Schema(description = "父级产品分类代码")
    @TableField("parent_code")
    private String parentCode;


    /** 产品分类名称 */
    @Schema(description = "产品分类名称")
    @TableField("category_name")
    private String categoryName;


    /** 产品分类说明 */
    @Schema(description = "产品分类说明")
    @TableField("category_desc")
    private String categoryDesc;


    /** 顺序号 */
    @Schema(description = "顺序号")
    @TableField("seq_no")
    private Integer seqNo;


    /** 是否有效0无效1有效 */
    @Schema(description = "是否有效0无效1有效")
    @TableField("is_valid")
    private Integer isValid;

}
