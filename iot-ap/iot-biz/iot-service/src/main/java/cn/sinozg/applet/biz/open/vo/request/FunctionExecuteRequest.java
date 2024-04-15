package cn.sinozg.applet.biz.open.vo.request;

import cn.sinozg.applet.biz.task.vo.request.param.TaskFunctionParamRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-03-07 20:16
 */
@Data
public class FunctionExecuteRequest {

    @Schema(description = "产品key")
    @NotBlank(message = "产品key不能为空！")
    private String prodKey;

    @Schema(description = "设备code")
    @NotBlank(message = "设备code不能为空！")
    private String deviceCode;

    /** 功能ID */
    @Schema(description = "功能ID")
    @NotBlank(message = "功能ID不能为空！")
    private String functionId;

    @Valid
    @Schema(description = "输入参数")
    private List<TaskFunctionParamRequest> args;
}
