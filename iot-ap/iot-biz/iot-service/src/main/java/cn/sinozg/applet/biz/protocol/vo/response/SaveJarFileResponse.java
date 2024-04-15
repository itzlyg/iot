package cn.sinozg.applet.biz.protocol.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-20 22:58
 */
@Data
public class SaveJarFileResponse {

    @Schema(description = "jar包的id")
    private String jarId;
    @Schema(description = "jar包的名称")
    private String jarName;
}
