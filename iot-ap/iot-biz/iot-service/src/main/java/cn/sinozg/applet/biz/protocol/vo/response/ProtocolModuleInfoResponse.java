package cn.sinozg.applet.biz.protocol.vo.response;

import cn.sinozg.applet.biz.protocol.vo.module.ProtocolConfigHttp;
import cn.sinozg.applet.biz.protocol.vo.module.ProtocolConfigMqtt;
import cn.sinozg.applet.biz.protocol.vo.module.ProtocolConfigTcp;
import cn.sinozg.applet.biz.protocol.vo.module.ProtocolConfigWebSocket;
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
@Schema(name = "ProtocolModuleInfoResponse", description = "协议组件信息 详情返回信息")
public class ProtocolModuleInfoResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 主键id */
    @Schema(description = "主键id")
    private String id;

    /** 协议名称 */
    @Schema(description = "协议名称")
    private String protocolName;

    /** 组件类型;01设备 02业务 */
    @Schema(description = "组件类型;01设备 02业务")
    private String moduleType;

    /** 解析器类型;01自定义 02静态 */
    @Schema(description = "解析器类型;01自定义 02静态")
    private String analysisType;

    /** 解析器id */
    @Schema(description = "解析器id")
    private String analysisId;

    /** 协议类型 */
    @Schema(description = "协议类型")
    @DictTrans(type = DictType.PROTOCOL_TYPE)
    private String protocolType;

    /** jar包id */
    @Schema(description = "jar包id")
    private String jarId;

    /** jar包name */
    @Schema(description = "jar包name")
    private String jarName;

    /** 协议类型 */
    @Schema(description = "mqtt 配置信息")
    private ProtocolConfigMqtt mqttConfig;

    @Schema(description = "tcp 配置信息")
    private ProtocolConfigTcp tcpConfig;

    @Schema(description = "http 配置信息")
    private ProtocolConfigHttp httpConfig;

    @Schema(description = "websocket 配置信息")
    private ProtocolConfigWebSocket webConfig;
}
