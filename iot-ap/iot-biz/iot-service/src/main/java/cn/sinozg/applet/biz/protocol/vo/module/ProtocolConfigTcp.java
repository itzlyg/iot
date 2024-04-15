package cn.sinozg.applet.biz.protocol.vo.module;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
* 协议组件信息表 tcp 配置
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
@Getter
@Setter
public class ProtocolConfigTcp extends BaseProtocolConfig {

    /** 模式;01 客户端 02 服务端 */
    @Schema(description = "模式;01 客户端 02 服务端")
    @NotBlank(message = "模式不能为空！")
    private String mode;

    /** ip */
    @Schema(description = "ip")
    @NotBlank(message = "ip不能为空！")
    private String ip;

    /** 分割方式 */
    @Schema(description = "分割方式")
    private String parserType;
}
