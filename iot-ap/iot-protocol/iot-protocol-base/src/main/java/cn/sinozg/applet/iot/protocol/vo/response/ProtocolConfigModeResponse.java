package cn.sinozg.applet.iot.protocol.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 协议配置信息 解析
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-11-16 12:11:35
 */
@Data
public class ProtocolConfigModeResponse {

    @Schema(description = "模式;01 客户端 02 服务端")
    private String mode;
}
