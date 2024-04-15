package cn.sinozg.applet.biz.task.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* 任务管理表表 详情返回信息
* @Description
* @Copyright Copyright (c) 2023
* @author zy
* @since 2023-12-18 22:35:41
*/
@Data
@Schema(name = "TaskManagerInfoResponse", description = "任务管理表 详情返回信息")
public class TaskManagerInfoResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 主键id */
    @Schema(description = "主键id")
    private String id;

    /** 任务事务主键 */
    @Schema(description = "任务事务主键")
    private String taskTransactionId;

    /** 任务名称 */
    @Schema(description = "任务名称")
    private String taskName;

    /** 执行时间 */
    @Schema(description = "执行时间")
    private LocalDateTime executeTime;

    /** 执行状态 */
    @Schema(description = "执行状态")
    private String executeStatus;

    /** 执行参数 */
    @Schema(description = "执行参数")
    private String executeParameters;

    /** 设备ID */
    @Schema(description = "设备ID")
    private String deviceId;

    /** 设备名称 */
    @Schema(description = "设备名称")
    private String deviceName;

    /** 功能ID */
    @Schema(description = "功能ID")
    private String functionId;

    /** 功能名称 */
    @Schema(description = "功能名称")
    private String functionName;


}
