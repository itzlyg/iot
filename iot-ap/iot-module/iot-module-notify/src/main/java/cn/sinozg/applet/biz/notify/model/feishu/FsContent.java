package cn.sinozg.applet.biz.notify.model.feishu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-19 19:04
 */
@Data
public class FsContent {

    @Schema(description = "请求")
    public FsPost post;
}
