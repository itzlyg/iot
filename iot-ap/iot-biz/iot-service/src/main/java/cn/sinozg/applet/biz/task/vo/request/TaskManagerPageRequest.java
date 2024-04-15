package cn.sinozg.applet.biz.task.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
* 任务管理表表 分页请求参数
* @Description
* @Copyright Copyright (c) 2023
* @author zy
* @since 2023-12-18 22:35:41
*/
@Data
@Schema(name = "TaskManagerPageRequest", description = "任务管理表 分页请求参数")
public class TaskManagerPageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "设备id不能为空！")
    @Schema(description = "设备id")
    private String deviceId;

    @Schema(description = "开始时间 yyyy-MM-dd")
    private String beginTime;

    @Schema(description = "结束时间 yyyy-MM-dd")
    private String endTime;
}
