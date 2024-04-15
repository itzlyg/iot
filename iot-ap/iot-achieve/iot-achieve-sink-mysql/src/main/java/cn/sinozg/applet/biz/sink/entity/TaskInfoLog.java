package cn.sinozg.applet.biz.sink.entity;

import cn.sinozg.applet.common.core.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 任务日志表
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-03-07 15:35:53
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sink_task_info_log")
@Schema(name = "TaskInfoLog", description = "任务日志")
public class TaskInfoLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /** 任务id */
    @Schema(description = "任务id")
    @TableField("task_id")
    private String taskId;


    /** 上报时间 */
    @Schema(description = "上报时间")
    @TableField("ts")
    private Long ts;


    /** 日志文本 */
    @Schema(description = "日志文本")
    @TableField("content")
    private String content;


    /** 是否成功 */
    @Schema(description = "是否成功")
    @TableField("success")
    private Boolean success;
}
