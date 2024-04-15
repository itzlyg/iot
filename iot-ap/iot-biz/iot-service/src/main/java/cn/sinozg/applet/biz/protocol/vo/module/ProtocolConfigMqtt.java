package cn.sinozg.applet.biz.protocol.vo.module;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
* 协议组件信息表 http 配置
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
@Getter
@Setter
public class ProtocolConfigMqtt extends BaseProtocolConfig {

    /** 是否使用WebSocket;01 是 02否 */
    @Schema(description = "是否使用WebSocket;01 是 02否")
    @NotBlank(message = "请选择是否使用WebSocket！")
    private String websocket;

    /** 模式;01 客户端 02 服务端 */
    @Schema(description = "模式;01 客户端 02 服务端")
    @NotBlank(message = "模式不能为空！")
    private String mode;

    /** 认证端口 */
    @Schema(description = "认证端口")
    @Min(value = 1, message = "认证端口不能小于1")
    @Max(value = 65535, message = "认证端口不能大于65535")
    private Integer authPort;

    /** ip */
    @Schema(description = "ip")
    private String ip;

    /** 客户端 */
    @Schema(description = "客户端")
    private String clientId;

    /** 用户名 */
    @Schema(description = "用户名")
    private String userName;

    /** 密码 */
    @Schema(description = "密码")
    private String password;

    /** 订阅主题 */
    @Schema(description = "订阅主题")
    private List<String> subscribe;

}
