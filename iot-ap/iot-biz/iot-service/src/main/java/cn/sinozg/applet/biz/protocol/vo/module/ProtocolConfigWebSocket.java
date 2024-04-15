package cn.sinozg.applet.biz.protocol.vo.module;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
* 协议组件信息表 websocket 配置
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
@Getter
@Setter
public class ProtocolConfigWebSocket extends BaseProtocolConfig {

    /** 模式;01 客户端 02 服务端 */
    @Schema(description = "模式;01 客户端 02 服务端")
    @NotBlank(message = "模式不能为空！")
    private String mode;

    /** ip */
    @Schema(description = "ip 服务端模式必填")
    private String ip;

    /** WebSocket模式下的 地址 */
    @Schema(description = "WebSocket模式下的 地址 服务端模式必填")
    private String websocketUrl;

    /** 心跳 */
    @Schema(description = "心跳")
    private Integer beatTime;

    /** 心跳数据 */
    @Schema(description = "心跳数据")
    private String beatData;

    /** 其他配置 */
    @Schema(description = "其他配置")
    private String otherConfig;
}
