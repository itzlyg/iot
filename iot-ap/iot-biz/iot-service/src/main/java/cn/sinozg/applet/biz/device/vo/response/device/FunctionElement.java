package cn.sinozg.applet.biz.device.vo.response.device;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @version V1.0
 * @Description:
 * @author: zhous
 * @date: 2023/12/12
 */
@Data
public class FunctionElement {
    @Schema(description = "功能名称")
    private String identifier;
    @Schema(description = "功能名称")
    private String name;
    @Schema(description = "功能参数定义")
    private List<FunctionParamElement> paramList;
}
