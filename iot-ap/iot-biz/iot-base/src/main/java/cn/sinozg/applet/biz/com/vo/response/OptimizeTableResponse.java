package cn.sinozg.applet.biz.com.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 优化表结构返回数据
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-05-10 19:49
 */
@Data
public class OptimizeTableResponse {
    @Schema(description = "表名称")
    private String table;
    @Schema(description = "操作")
    private String op;
    @Schema(description = "消息类型")
    private String msgType;
    @Schema(description = "消息文本")
    private String msgText;
}
