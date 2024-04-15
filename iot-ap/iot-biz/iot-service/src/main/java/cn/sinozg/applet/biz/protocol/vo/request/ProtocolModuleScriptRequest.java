package cn.sinozg.applet.biz.protocol.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
* 协议组件信息表 脚本信息
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
@Data
public class ProtocolModuleScriptRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 主键id */
    @Schema(description = "主键id")
    @NotBlank(message = "协议id不能为空！")
    private String id;

    /** 组件脚本类型 */
    @Schema(description = "组件脚本类型")
    @NotBlank(message = "组件脚本类型不能为空！")
    private String protocolScriptType;

    @Schema(description = "组件脚本")
    @NotBlank(message = "组件脚本不能为空！")
    private String script;
}
