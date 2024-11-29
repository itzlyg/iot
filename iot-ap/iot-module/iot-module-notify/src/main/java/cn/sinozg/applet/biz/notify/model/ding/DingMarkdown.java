package cn.sinozg.applet.biz.notify.model.ding;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-19 17:56
 */
@Data
public class DingMarkdown {

    @Schema(description = "消息内容")
    private String content;
}
