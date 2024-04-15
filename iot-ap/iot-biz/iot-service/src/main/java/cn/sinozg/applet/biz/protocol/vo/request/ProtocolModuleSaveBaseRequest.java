package cn.sinozg.applet.biz.protocol.vo.request;

import cn.sinozg.applet.biz.protocol.vo.module.ProtocolConfigHttp;
import cn.sinozg.applet.biz.protocol.vo.module.ProtocolConfigMqtt;
import cn.sinozg.applet.biz.protocol.vo.module.ProtocolConfigTcp;
import cn.sinozg.applet.biz.protocol.vo.module.ProtocolConfigWebSocket;
import cn.sinozg.applet.biz.protocol.vo.valid.ProtocolModuleConfigValidation;
import cn.sinozg.applet.biz.protocol.vo.valid.ProtocolModuleConfigValidationProvider;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.group.GroupSequenceProvider;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
* 协议组件信息表 新增请求参数
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
@Getter
@Setter
@GroupSequenceProvider(value = ProtocolModuleConfigValidationProvider.class)
public class ProtocolModuleSaveBaseRequest {

    /** 协议名称 */
    @Schema(description = "协议名称")
    @NotBlank(message = "协议名称不能为空！")
    private String protocolName;

    /** 组件类型;01设备 02业务 */
    @Schema(description = "组件类型;01设备 02业务")
    private String moduleType;

    /** 解析器类型;01自定义 02静态 */
    @Schema(description = "解析器类型;01自定义 02静态")
    @NotBlank(message = "转码器类型不能为空！")
    private String analysisType;

    /** 解析器id */
    @Schema(description = "解析器id")
    private String analysisId;

    /** 协议类型 */
    @Schema(description = "协议类型")
    @NotBlank(message = "协议类型不能为空！")
    private String protocolType;

    /** jar包id */
    @Schema(description = "jar包id")
    @NotBlank(message = "请上传jar包！")
    private String jarId;

    /** jar包name */
    @Schema(description = "jar包name")
    private String jarName;

    /** 协议类型 */
    @Valid
    @Schema(description = "mqtt 配置信息")
    @NotNull(groups = ProtocolModuleConfigValidation.Mqtt.class, message = "mqtt 配置信息不能为空！")
    private ProtocolConfigMqtt mqttConfig;

    @Valid
    @Schema(description = "tcp 配置信息")
    @NotNull(groups = ProtocolModuleConfigValidation.Tcp.class, message = "tcp 配置信息不能为空！")
    private ProtocolConfigTcp tcpConfig;

    @Valid
    @Schema(description = "http 配置信息")
    @NotNull(groups = ProtocolModuleConfigValidation.Http.class, message = "http 配置信息不能为空！")
    private ProtocolConfigHttp httpConfig;

    @Valid
    @Schema(description = "websocket 配置信息")
    @NotNull(groups = ProtocolModuleConfigValidation.WebSocket.class, message = "websocket 配置信息不能为空！")
    private ProtocolConfigWebSocket webConfig;
}
