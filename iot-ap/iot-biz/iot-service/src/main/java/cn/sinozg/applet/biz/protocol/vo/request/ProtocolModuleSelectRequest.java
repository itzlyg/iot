package cn.sinozg.applet.biz.protocol.vo.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* 协议组件信息表 分页请求参数
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
@Data
public class ProtocolModuleSelectRequest {

    @JsonIgnore
    private String uid;

    @Schema(description = "协议名称")
    private String protocolName;
}
