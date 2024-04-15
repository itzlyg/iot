package cn.sinozg.applet.biz.sink.vo.request;

import lombok.Data;

/**
 * @Description 
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-12-07 15:30:52
 */
@Data
public class TaskLogRecordSinkRequest {

    private String id;

    private String taskId;

    private String content;

    private Boolean success;
}
