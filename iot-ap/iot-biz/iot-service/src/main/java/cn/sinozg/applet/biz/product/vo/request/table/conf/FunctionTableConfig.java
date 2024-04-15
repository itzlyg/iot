package cn.sinozg.applet.biz.product.vo.request.table.conf;

import cn.sinozg.applet.common.annotation.DictTrans;
import cn.sinozg.applet.common.annotation.Regex;
import cn.sinozg.applet.common.enums.DictType;
import cn.sinozg.applet.common.enums.RegexType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @version V1.0
 * @Description:
 * @author: zhous
 * @date: 2023/12/4
 */
@Data
public class FunctionTableConfig {

    @Regex(type = RegexType.FIELD)
    @Schema(description = "参数标识")
    private String parameterIdentifier;

    @Schema(description = "参数名称")
    private String parameterName;

    @Schema(description = "是否必填")
    private boolean isMandatory;

    @Schema(description = "数据类型")
    @DictTrans(type = DictType.DATA_TYPE)
    private String dataType;

    @Schema(description = "其他配置")
    private String otherConfiguration;

}
