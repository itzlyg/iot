package cn.sinozg.applet.biz.notify.model.feishu;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 飞书
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-19 19:03
 */
@Data
public class FsModel {

    @Schema(description = "消息类型")
    @JsonProperty("msg_type")
    private String msgType = "post";

    @Schema(description = "内容")
    private FsContent content;
}
