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
public class EventTableConfig {

    @Regex(type = RegexType.FIELD)
    @Schema(description = "用于区分不同参数的标识")
    private String parameterIdentifier;

    @Schema(description = "参数的名称")
    private String parameterName;

    @Schema(description = "标识该参数是否为必填项")
    private boolean isMandatory;

    @Schema(description = "该参数的数据类型")
    @DictTrans(type = DictType.DATA_TYPE)
    private String dataType;

    @Schema(description = "其他与参数相关的配置")
    private String otherConfiguration;

}
