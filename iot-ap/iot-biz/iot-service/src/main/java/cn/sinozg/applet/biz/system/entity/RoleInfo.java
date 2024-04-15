package cn.sinozg.applet.biz.system.entity;

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
* 角色信息表表
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-09 16:04:37
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role_info")
@Schema(name = "RoleInfo", description = "角色信息表")
public class RoleInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /** 用户id */
    @Schema(description = "用户id")
    @TableField("user_id")
    private String userId;


    /** 角色编码 */
    @Schema(description = "角色编码")
    @TableField("role_code")
    private String roleCode;


    /** 角色名称 */
    @Schema(description = "角色名称")
    @TableField("role_name")
    private String roleName;


    /** 角色描述 */
    @Schema(description = "角色描述")
    @TableField("role_desc")
    private String roleDesc;


    /** 系统角色 */
    @Schema(description = "系统角色")
    @TableField("sys_role")
    private String sysRole;


    /** 租户id */
    @Schema(description = "租户id")
    @TableField(value = "tenant_id", updateStrategy = FieldStrategy.NEVER)
    private String tenantId;

}
