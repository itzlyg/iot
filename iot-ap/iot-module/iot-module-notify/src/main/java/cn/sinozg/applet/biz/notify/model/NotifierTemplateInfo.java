package cn.sinozg.applet.biz.notify.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author: xyb
 * @Description:
 * @Date: 2023-12-30 下午 01:03
 **/
@Data
public class NotifierTemplateInfo {
    @Schema(description = "名称")
    private String name;
    @Schema(description = "消息发送类型")
    private String type;
    @Schema(description = "是否为预设模板: true-预设模板 false-自定义模板")
    private boolean preset = false;
    @Schema(description = "消息内容")
    private String content;
}