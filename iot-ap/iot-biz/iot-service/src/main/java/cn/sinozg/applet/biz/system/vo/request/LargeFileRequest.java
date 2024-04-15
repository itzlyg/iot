package cn.sinozg.applet.biz.system.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-09-08 15:37
 */
@Data
public class LargeFileRequest {

    @Schema(description = "文件名称")
    private String fileName;

    @Schema(description = "业务id")
    private String bizId;

    @Schema(description = "文件大小")
    private Long fileSize;
}
