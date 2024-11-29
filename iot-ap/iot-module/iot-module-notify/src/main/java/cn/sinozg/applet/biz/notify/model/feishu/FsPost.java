package cn.sinozg.applet.biz.notify.model.feishu;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-19 19:05
 */
@Data
public class FsPost {

    @JsonProperty("zh_cn")
    @Schema(description = "语言")
    public FsLanguage language;
}
