package cn.sinozg.applet.biz.sink.vo.response;

import cn.sinozg.applet.common.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 统计的时间数据
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-12-06 19:01:45
 */
@Data
public class TmStaResponse {

    /** 时间 */
    @JsonIgnore
    private long ts;

    @Schema(description = "数据值")
    private long data;

    private LocalDateTime dateTime;

    public LocalDateTime getDateTime() {
        if (dateTime == null) {
            dateTime = DateUtil.ldt(ts);
        }
        return dateTime;
    }
}
