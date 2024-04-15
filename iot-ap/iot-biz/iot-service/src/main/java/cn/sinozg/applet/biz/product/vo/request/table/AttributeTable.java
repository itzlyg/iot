package cn.sinozg.applet.biz.product.vo.request.table;

import cn.sinozg.applet.common.annotation.DictTrans;
import cn.sinozg.applet.common.annotation.Regex;
import cn.sinozg.applet.common.enums.DictType;
import cn.sinozg.applet.common.enums.RegexType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @version V1.0
 * @Description: 属性table配置
 * @author: zhous
 * @date: 2023/12/4
 */
@Data
public class AttributeTable {

    @Schema(description = "序号:唯一标识一个属性表格中的条目")
    private int sequenceNumber;

    @Schema(description = "标识:唯一标识一个属性表格中的条目")
    @Regex(type = RegexType.FIELD)
    private String identifier;

    @Schema(description = "名称:属性表格中条目的名称")
    private String name;

    @Schema(description = "数据类型:属性表格中条目的数据类型 1:int 2:long 3:float 4:double 5:text")
    @DictTrans(type = DictType.DATA_TYPE)
    private String dataType;

    @Schema(description = "读写属性:标识属性表格中条目的读写属性")
    private boolean readWriteAttribute;
}
