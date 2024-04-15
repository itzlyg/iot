package cn.sinozg.applet.common.properties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 协议配置
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-16 18:54
 */
@Data
public class ProtocolProperties {

    @Schema(description = "组件脚本目录")
    private String moduleDir;

    @Schema(description = "解析转换器目录")
    private String analysisDir;

    @Schema(description = "jar包的目录")
    private String jarDir;
}
