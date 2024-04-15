package cn.sinozg.applet.biz.task.vo.request.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @version V1.0
 * @Description:
 * @author: zhous
 * @date: 2023/12/18
 */
@Data
public class TaskFunctionParamRequest {

    @Schema(description = "参数标识")
    @NotBlank(message = "参数标识不能为空！")
    private String parameterIdentifier;

    @Schema(description = "参数值")
    @NotBlank(message = "参数值不能为空！")
    private String value;
}
