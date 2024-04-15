package cn.sinozg.applet.biz.task.vo.request;

import cn.sinozg.applet.biz.task.vo.request.param.TaskFunctionParamRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
* 任务管理表表 新增请求参数
* @Description
* @Copyright Copyright (c) 2023
* @author zy
* @since 2023-12-18 22:35:41
*/
@Data
@Schema(name = "TaskManagerCreateRequest", description = "任务管理表 新增请求参数")
public class TaskManagerCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 任务名称 */
    @Schema(description = "任务名称")
    private String taskName;

    /** 执行参数 */
    @NotNull
    @Valid
    @Schema(description = "执行参数")
    private List<TaskFunctionParamRequest> executionParams;

    /** 设备ID */
    @Schema(description = "设备ID")
    @NotBlank(message = "设备id不能为空！")
    private String deviceId;

    /** 设备名称 */
    @Schema(description = "设备名称")
    private String deviceName;

    /** 功能ID */
    @Schema(description = "功能ID")
    @NotBlank(message = "功能ID不能为空！")
    private String functionId;

    /** 功能名称 */
    @Schema(description = "功能名称")
    private String functionName;
}
