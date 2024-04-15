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
* 用户角色表表
* @Description
* @Copyright Copyright (c) 2023
* @author xyb
* @since 2023-04-11 20:40:45
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user_role")
@Schema(name = "UserRole", description = "用户角色表")
public class UserRole extends BaseEntity {

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

}
