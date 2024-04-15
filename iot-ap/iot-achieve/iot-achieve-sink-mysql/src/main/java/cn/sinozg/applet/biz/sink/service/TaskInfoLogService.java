package cn.sinozg.applet.biz.sink.service;

import cn.sinozg.applet.biz.sink.entity.TaskInfoLog;
import cn.sinozg.applet.biz.sink.vo.response.TaskLogRecordPageResponse;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.PagingRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* 任务日志 服务类
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-03-07 15:35:53
*/
public interface TaskInfoLogService extends IService<TaskInfoLog> {

    void deleteByTaskId (String taskId);

    void addTaskLog (String taskId, String content, boolean success);

    BasePageResponse<List<TaskLogRecordPageResponse>> taskIdPage(PagingRequest page, String taskId);
}
