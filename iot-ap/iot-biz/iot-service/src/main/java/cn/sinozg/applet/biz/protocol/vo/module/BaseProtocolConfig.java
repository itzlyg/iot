package cn.sinozg.applet.biz.protocol.vo.module;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
* 协议组件信息表 基础
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
@Getter
@Setter
public abstract class BaseProtocolConfig {
    /** 端口 */
    @Schema(description = "端口")
    @Min(value = 1, message = "端口不能小于1")
    @Max(value = 65535, message = "端口不能大于65535")
    @NotNull(message = "端口不能为空！")
    private Integer port;
}
