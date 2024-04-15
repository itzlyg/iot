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
* 父子流程实例映射表表
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-01-09 13:40:56
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_scene_flow_instance_mapping")
@Schema(name = "FlowInstanceMapping", description = "父子流程实例映射表")
public class FlowInstanceMapping extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /** 流程执行实例id */
    @Schema(description = "流程执行实例id")
    @TableField("flow_instance_id")
    private String flowInstanceId;


    /** 节点执行实例id */
    @Schema(description = "节点执行实例id")
    @TableField("node_instance_id")
    private String nodeInstanceId;


    /** 节点唯一标识 */
    @Schema(description = "节点唯一标识")
    @TableField("node_key")
    private String nodeKey;


    /** 子流程执行实例id */
    @Schema(description = "子流程执行实例id")
    @TableField("sub_flow_instance_id")
    private String subFlowInstanceId;


    /** 状态(1.执行 2.回滚) */
    @Schema(description = "状态(1.执行 2.回滚)")
    @TableField("type")
    private Integer type;


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
