package cn.sinozg.applet.biz.sink.vo.response;

import cn.sinozg.applet.common.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description 
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-12-07 15:30:52
 */
@Data
public class TaskLogRecordPageResponse {

    private String taskId;

    private String content;

    private Boolean success;

    @JsonIgnore
    private Long ts;

    private LocalDateTime dateTime;

    public LocalDateTime getDateTime() {
        return DateUtil.ldt(ts);
    }
}
