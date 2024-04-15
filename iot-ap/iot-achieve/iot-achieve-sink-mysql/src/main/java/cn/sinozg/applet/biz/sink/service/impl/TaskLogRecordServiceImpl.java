package cn.sinozg.applet.biz.sink.service.impl;

import cn.sinozg.applet.biz.sink.service.TaskInfoLogService;
import cn.sinozg.applet.biz.sink.service.TaskLogRecordService;
import cn.sinozg.applet.biz.sink.vo.request.TaskLogRecordSinkRequest;
import cn.sinozg.applet.biz.sink.vo.response.TaskLogRecordPageResponse;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.PagingRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 任务日志
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-07 15:34
 */
@Slf4j
@Service
public class TaskLogRecordServiceImpl implements TaskLogRecordService {

    @Resource
    private TaskInfoLogService taskInfoLogService;
    @Override
    public void deleteByTaskId(String taskId) {
        taskInfoLogService.deleteByTaskId(taskId);
    }

    @Override
    public BasePageResponse<List<TaskLogRecordPageResponse>> taskIdPage(PagingRequest page, String taskId) {
        return taskInfoLogService.taskIdPage(page, taskId);
    }

    @Override
    public void add(TaskLogRecordSinkRequest params) {
        taskInfoLogService.addTaskLog(params.getTaskId(), params.getContent(), params.getSuccess());
    }
}
