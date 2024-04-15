package cn.sinozg.applet.biz.protocol.vo.response;

import cn.sinozg.applet.common.annotation.DictTrans;
import cn.sinozg.applet.common.enums.DictType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
* 协议组件信息表 分页返回参数
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
@Data
@Schema(name = "ProtocolModulePageResponse", description = "协议组件信息 分页返回参数")
public class ProtocolModulePageResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 主键id */
    @Schema(description = "主键id")
    private String id;

    /** 协议名称 */
    @Schema(description = "协议名称")
    private String protocolName;

    /** 解析器id */
    @Schema(description = "解析器名称")
    private String analysisName;

    /** 协议类型 */
    @Schema(description = "协议类型")
    @DictTrans(type = DictType.PROTOCOL_TYPE)
    private String protocolType;

    /** jar包id */
    @Schema(description = "jar包名称")
    private String jarName;

    /** 数据状态;00 未启用 01 启用 */
    @Schema(description = "数据状态;00 未启用 01 启用")
    @DictTrans(type = DictType.DATA_STATUS)
    private String dataStatus;

}
