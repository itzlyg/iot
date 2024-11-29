package cn.sinozg.applet.biz.notify.model.feishu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-19 19:05
 */
@Data
public class FsLanguage {

    @Schema(description = "标题")
    public String title;

    @Schema(description = "内容")
    public List<List<FsContentDetail>> content;
}
