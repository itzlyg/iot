package cn.sinozg.applet.biz.sink.service;

import cn.sinozg.applet.biz.sink.vo.request.TaskLogRecordSinkRequest;
import cn.sinozg.applet.biz.sink.vo.response.TaskLogRecordPageResponse;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.PagingRequest;

import java.util.List;

/**
 * 任务日志记录查询
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-12-07 15:33:58
 */
public interface TaskLogRecordService {
    void deleteByTaskId(String taskId);

    BasePageResponse<List<TaskLogRecordPageResponse>> taskIdPage(PagingRequest page, String taskId);

    void add(TaskLogRecordSinkRequest params);
}
