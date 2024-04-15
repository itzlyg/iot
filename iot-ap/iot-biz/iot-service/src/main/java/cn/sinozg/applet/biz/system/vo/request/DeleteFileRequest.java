package cn.sinozg.applet.biz.system.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-08-29 14:33
 */
@Data
public class DeleteFileRequest {

    @Schema(description = "业务id")
    private String bizId;

    @Schema(description = "文件id集合")
    private List<String> ids;
}
