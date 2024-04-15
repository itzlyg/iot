package cn.sinozg.applet.biz.product.vo.request.table;

import cn.sinozg.applet.biz.product.vo.request.table.conf.EventTableConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

/**
 * @version V1.0
 * @Description: 事件talbe配置
 * @author: zhous
 * @date: 2023/12/4
 */
@Data
public class EventTable {

    @Schema(description = "序号，唯一标识一个事件表格")
    private int sequenceNumber;

    @Schema(description = "标识，用于区分不同的事件表格")
    private String identifier;

    @Schema(description = "事件表格的名称")
    private String name;

    @Schema(description = "事件级别的描述")
    private String eventLevel;

    @Valid
    @Schema(description = "事件表格的输出参数")
    private List<EventTableConfig> outputParameters;

    @Schema(description = "事件表格的配置信息")
    private String configuration;

    @Schema(description = "事件表格的说明")
    private String description;
}
