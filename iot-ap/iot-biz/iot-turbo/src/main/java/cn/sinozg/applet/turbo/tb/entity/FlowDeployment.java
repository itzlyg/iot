package cn.sinozg.applet.turbo.tb.entity;

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
* 流程部署表表
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-01-09 13:40:56
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_scene_flow_deployment")
@Schema(name = "FlowDeployment", description = "流程部署表")
public class FlowDeployment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /** 流程模型部署id */
    @Schema(description = "流程模型部署id")
    @TableField("flow_deploy_id")
    private String flowDeployId;


    /** 流程模型id */
    @Schema(description = "流程模型id")
    @TableField("flow_module_id")
    private String flowModuleId;


    /** 流程名称 */
    @Schema(description = "流程名称")
    @TableField("flow_name")
    private String flowName;


    /** 流程业务标识 */
    @Schema(description = "流程业务标识")
    @TableField("flow_key")
    private String flowKey;


    /** 表单定义 */
    @Schema(description = "表单定义")
    @TableField("flow_model")
    private String flowModel;


    /** 状态(0.默认值 1.已部署 3.已下线) */
    @Schema(description = "状态(0.默认值 1.已部署 3.已下线)")
    @TableField("status")
    private Integer status;


    /** 归档状态(0未删除，1删除) */
    @Schema(description = "归档状态(0未删除，1删除)")
    @TableField("archive")
    private Integer archive;


    /** 租户 */
    @Schema(description = "租户")
    @TableField("tenant")
    private String tenant;


    /** 调用方 */
    @Schema(description = "调用方")
    @TableField("caller")
    private String caller;


    /** 租户id */
    @Schema(description = "租户id")
    @TableField(value = "tenant_id", updateStrategy = FieldStrategy.NEVER)
    private String tenantId;

}
