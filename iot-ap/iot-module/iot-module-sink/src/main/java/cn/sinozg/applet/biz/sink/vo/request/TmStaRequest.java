package cn.sinozg.applet.biz.sink.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 统计的时间数据
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-12-06 19:01:45
 */
@Data
public class TmStaRequest {

    @Schema(description = "用户id")
    private String uid;
    @Schema(description = "开始")
    private long start;
    @Schema(description = "结束")
    private long end;
}
