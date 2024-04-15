package cn.sinozg.applet.biz.protocol.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
* 协议组件信息表 修改请求参数
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
@Getter
@Setter
public class ProtocolModuleUpdateRequest extends ProtocolModuleSaveBaseRequest {
    /** 主键id */
    @Schema(description = "主键id")
    @NotBlank(message = "id不能为空")
    private String id;
}
