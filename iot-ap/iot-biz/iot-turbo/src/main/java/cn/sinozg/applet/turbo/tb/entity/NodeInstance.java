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
* 节点执行实例表表
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-01-09 13:40:56
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_scene_node_instance")
@Schema(name = "NodeInstance", description = "节点执行实例表")
public class NodeInstance extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /** 节点执行实例id */
    @Schema(description = "节点执行实例id")
    @TableField("node_instance_id")
    private String nodeInstanceId;


    /** 流程执行实例id */
    @Schema(description = "流程执行实例id")
    @TableField("flow_instance_id")
    private String flowInstanceId;


    /** 上一个节点执行实例id */
    @Schema(description = "上一个节点执行实例id")
    @TableField("source_node_instance_id")
    private String sourceNodeInstanceId;


    /** 实例数据id */
    @Schema(description = "实例数据id")
    @TableField("instance_data_id")
    private String instanceDataId;


    /** 流程模型部署id */
    @Schema(description = "流程模型部署id")
    @TableField("flow_deploy_id")
    private String flowDeployId;


    /** 节点唯一标识 */
    @Schema(description = "节点唯一标识")
    @TableField("source_node_key")
    private String sourceNodeKey;


    /** 上一个流程节点唯一标识 */
    @Schema(description = "上一个流程节点唯一标识")
    @TableField("node_key")
    private String nodeKey;


    /** 状态(0.默认值 1.处理成功 2.处理中 3.处理失败 4.处理已撤销) */
    @Schema(description = "状态(0.默认值 1.处理成功 2.处理中 3.处理失败 4.处理已撤销)")
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
