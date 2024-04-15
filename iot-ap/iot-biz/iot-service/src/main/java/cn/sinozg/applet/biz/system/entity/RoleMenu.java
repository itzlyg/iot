package cn.sinozg.applet.biz.system.entity;

import cn.sinozg.applet.common.core.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 角色菜单表表
* @Description
* @Copyright Copyright (c) 2023
* @author xyb
* @since 2023-04-11 20:40:45
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role_menu")
@Schema(name = "RoleMenu", description = "角色菜单表")
public class RoleMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /** 角色编码 */
    @Schema(description = "角色编码")
    @TableField("role_code")
    private String roleCode;


    /** 菜单编码 */
    @Schema(description = "菜单编码")
    @TableField("resource_code")
    private String resourceCode;


    /** 类型;01 菜单 02按钮 */
    @Schema(description = "类型;01 菜单 02按钮")
    @TableField("resource_type")
    private String resourceType;


    /** 是否有效 00 正常，01 无效 */
    @Schema(description = "是否有效 00 正常，01 无效")
    @TableField("data_valid")
    private String dataValid;

}
