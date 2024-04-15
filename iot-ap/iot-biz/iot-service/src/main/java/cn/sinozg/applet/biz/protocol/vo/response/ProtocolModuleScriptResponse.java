package cn.sinozg.applet.biz.protocol.vo.response;

import cn.sinozg.applet.common.annotation.DictTrans;
import cn.sinozg.applet.common.enums.DictType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
* 协议组件信息表 详情返回信息
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
@Data
public class ProtocolModuleScriptResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 主键id */
    @Schema(description = "主键id")
    private String id;

    /** 组件脚本类型 */
    @Schema(description = "组件脚本类型")
    @DictTrans(type = DictType.SCRIPT_TYPE)
    private String protocolScriptType;

    @Schema(description = "组件脚本")
    private String script;
}
