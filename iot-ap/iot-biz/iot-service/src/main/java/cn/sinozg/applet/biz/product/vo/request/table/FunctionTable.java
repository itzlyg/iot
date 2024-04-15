package cn.sinozg.applet.biz.product.vo.request.table;

import cn.sinozg.applet.biz.product.vo.request.table.conf.FunctionTableConfig;
import cn.sinozg.applet.common.annotation.Regex;
import cn.sinozg.applet.common.enums.RegexType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

/**
 * @version V1.0
 * @Description: 功能描述table
 * @author: zhous
 * @date: 2023/12/4
 */
@Data
public class FunctionTable {

    @Regex(type = RegexType.FIELD)
    @Schema(description = "标识")
    private String identifier;

    @Schema(description = "名称")
    private String name;

    @Valid
    @Schema(description = "输入参数")
    private List<FunctionTableConfig> inputParameters;

    @Schema(description = "输出参数")
    private String outputParameters;

    @Schema(description = "说明")
    private String description;
}
