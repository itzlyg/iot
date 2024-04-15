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
* 部门信息表
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-09-13 11:13:12
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dept_info")
@Schema(name = "DeptInfo", description = "部门信息")
public class DeptInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /** 部门名称 */
    @Schema(description = "部门名称")
    @TableField("dept_name")
    private String deptName;


    /** 父部门id */
    @Schema(description = "父部门id")
    @TableField("pater_id")
    private String paterId;


    /** 负责人 */
    @Schema(description = "负责人")
    @TableField("director")
    private String director;


    /** 联系方式 */
    @Schema(description = "联系方式")
    @TableField("phone_num")
    private String phoneNum;


    /** 邮箱 */
    @Schema(description = "邮箱")
    @TableField("email")
    private String email;

    /** 租户id */
    @Schema(description = "租户id")
    @TableField(value = "tenant_id", updateStrategy = FieldStrategy.NEVER)
    private String tenantId;

}
