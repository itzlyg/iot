package cn.sinozg.applet.biz.system.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 大文件上传保存的信息
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-09-08 15:23
 */
@Data
public class LargeFileResponse {

    /** 文件名称 **/
    @Schema(description = "文件id")
    private String id;

    @Schema(description = "七牛的token")
    private String token;
}
