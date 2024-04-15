package cn.sinozg.applet.biz.task.entity;

import cn.sinozg.applet.common.core.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
* 任务管理表表
* @Description
* @Copyright Copyright (c) 2023
* @author zy
* @since 2023-12-18 22:35:41
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_set_task_manager")
@Schema(name = "TaskManager", description = "任务管理表")
public class TaskManager extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /** 任务事务主键 */
    @Schema(description = "任务事务主键")
    @TableField("task_transaction_id")
    private String taskTransactionId;


    /** 任务名称 */
    @Schema(description = "任务名称")
    @TableField("task_name")
    private String taskName;


    /** 执行时间 */
    @Schema(description = "执行时间")
    @TableField("execute_time")
    private LocalDateTime executeTime;


    /** 执行状态 */
    @Schema(description = "执行状态")
    @TableField("execute_status")
    private String executeStatus;


    /** 执行参数 */
    @Schema(description = "执行参数")
    @TableField("execute_parameters")
    private String executeParameters;


    /** 设备ID */
    @Schema(description = "设备ID")
    @TableField("device_id")
    private String deviceId;


    /** 设备名称 */
    @Schema(description = "设备名称")
    @TableField("device_name")
    private String deviceName;


    /** 功能ID */
    @Schema(description = "功能ID")
    @TableField("function_id")
    private String functionId;


    /** 功能名称 */
    @Schema(description = "功能名称")
    @TableField("function_name")
    private String functionName;


    /** 租户id */
    @Schema(description = "租户id")
    @TableField(value = "tenant_id", updateStrategy = FieldStrategy.NEVER)
    private String tenantId;

}
