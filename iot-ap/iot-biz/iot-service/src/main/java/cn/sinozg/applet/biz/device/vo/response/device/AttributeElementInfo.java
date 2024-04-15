package cn.sinozg.applet.biz.device.vo.response.device;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @version V1.0
 * @Description:
 * @author: zhous
 * @date: 2023/12/12
 */
@Data
public class AttributeElementInfo {

    @Schema(description = "标识:唯一标识一个属性表格中的条目")
    private String identifier;

    @Schema(description = "名称:属性表格中条目的名称")
    private String name;

    @Schema(description = "对应属性最新的值")
    private Object value;
}
