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
* 流程执行实例表表
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-01-09 13:40:56
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_scene_flow_instance")
@Schema(name = "FlowInstance", description = "流程执行实例表")
public class FlowInstance extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /** 流程执行实例id */
    @Schema(description = "流程执行实例id")
    @TableField("flow_instance_id")
    private String flowInstanceId;


    /** 父流程执行实例id */
    @Schema(description = "父流程执行实例id")
    @TableField("parent_flow_instance_id")
    private String parentFlowInstanceId;


    /** 流程模型部署id */
    @Schema(description = "流程模型部署id")
    @TableField("flow_deploy_id")
    private String flowDeployId;


    /** 流程模型id */
    @Schema(description = "流程模型id")
    @TableField("flow_module_id")
    private String flowModuleId;


    /** 状态(0.默认值 1.执行完成 2.执行中 3.执行终止(强制终止) 4.执行结束) */
    @Schema(description = "状态(0.默认值 1.执行完成 2.执行中 3.执行终止(强制终止) 4.执行结束)")
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
