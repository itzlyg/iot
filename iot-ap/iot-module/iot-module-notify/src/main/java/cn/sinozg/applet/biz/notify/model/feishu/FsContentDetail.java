package cn.sinozg.applet.biz.notify.model.feishu;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-19 19:04
 */
@Data
public class FsContentDetail {

    @Schema(description = "格式  目前支持文本、超链接、@人的功能  text  a  at")
    public String tag;

    @Schema(description = "文本 内容")
    public String text;

    @Schema(description = "超链接地址")
    public String href;

    @JsonProperty("user_id")
    public String userId;

    @JsonProperty("user_name")
    public String userName;
}
