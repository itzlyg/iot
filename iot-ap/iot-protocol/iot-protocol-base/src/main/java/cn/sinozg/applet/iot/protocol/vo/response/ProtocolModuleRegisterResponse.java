package cn.sinozg.applet.iot.protocol.vo.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 协议组件消息
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-11-16 12:11:35
 */
@Data
public class ProtocolModuleRegisterResponse {

    @Schema(description = "协议id")
    private String id;
    @Schema(description = "协议名称")
    private String protocolName;
    @Schema(description = "组件类型")
    private String moduleType;
    @Schema(description = "协议类型")
    private String protocolType;
    @Schema(description = "jar包id")
    private String jarId;
    @Schema(description = "配置")
    private String config;
    @Schema(description = "数据解析器id")
    private String analysisId;
    @Schema(description = "数据解析器类型")
    private String analysisType;
    @Schema(description = "协议解析类型")
    private String protocolScriptType;

    @JsonIgnore
    @Schema(description = "模式;01 客户端 02 服务端")
    private String mode;
}
