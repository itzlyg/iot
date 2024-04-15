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
* 用户信息表表
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-09 16:04:37
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user_info")
@Schema(name = "UserInfo", description = "用户信息表")
public class UserInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @Schema(description = "用户ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /** 用户账号 */
    @Schema(description = "用户账号")
    @TableField("user_name")
    private String userName;


    /** 用户昵称 */
    @Schema(description = "用户昵称")
    @TableField("nick_name")
    private String nickName;


    /** 部门ID */
    @Schema(description = "部门ID")
    @TableField("dept_id")
    private String deptId;


    /** 密码 */
    @Schema(description = "密码")
    @TableField("pass_word")
    private String passWord;


    /** 用户邮箱 */
    @Schema(description = "用户邮箱")
    @TableField("email")
    private String email;


    /** 手机号码 */
    @Schema(description = "手机号码")
    @TableField("phone_num")
    private String phoneNum;


    /** 用户性别;00男 01女 02未知 */
    @Schema(description = "用户性别;00男 01女 02未知")
    @TableField("sex")
    private String sex;


    /** 头像地址 */
    @Schema(description = "头像地址")
    private String avatar;


    /** 用户类型;00：普通用户 01:子账户 */
    @Schema(description = "用户类型;00：普通用户 01:子账户")
    @TableField("user_type")
    private String userType;


    /** 租户id */
    @Schema(description = "租户id")
    @TableField(value = "tenant_id", updateStrategy = FieldStrategy.NEVER)
    private String tenantId;


    /** 帐号状态;00 未启用 01 启用 */
    @Schema(description = "帐号状态;00 未启用 01 启用")
    @TableField("data_status")
    private String dataStatus;

}
