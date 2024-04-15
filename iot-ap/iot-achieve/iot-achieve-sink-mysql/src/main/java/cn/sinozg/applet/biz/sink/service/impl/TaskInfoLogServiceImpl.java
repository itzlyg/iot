package cn.sinozg.applet.biz.sink.service.impl;

import cn.sinozg.applet.biz.sink.entity.TaskInfoLog;
import cn.sinozg.applet.biz.sink.mapper.TaskInfoLogMapper;
import cn.sinozg.applet.biz.sink.service.TaskInfoLogService;
import cn.sinozg.applet.biz.sink.vo.response.TaskLogRecordPageResponse;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.PagingRequest;
import cn.sinozg.applet.common.utils.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* 任务日志 服务实现类
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-03-07 15:35:53
*/
@Service
public class TaskInfoLogServiceImpl extends ServiceImpl<TaskInfoLogMapper, TaskInfoLog> implements TaskInfoLogService {

    @Resource
    private TaskInfoLogMapper mapper;

    @Override
    public void deleteByTaskId(String taskId) {
        this.remove(new LambdaQueryWrapper<TaskInfoLog>().eq(TaskInfoLog::getTaskId, taskId));
    }
    @Override
    public void addTaskLog (String taskId, String content, boolean success){
        TaskInfoLog taskLog = new TaskInfoLog();
        taskLog.setSuccess(success);
        taskLog.setContent(content);
        taskLog.setTaskId(taskId);
        taskLog.setTs(System.currentTimeMillis());
        this.save(taskLog);
    }

    @Override
    public BasePageResponse<List<TaskLogRecordPageResponse>> taskIdPage(PagingRequest page, String taskId) {
        PageUtil<TaskLogRecordPageResponse, String> pu = (p, q) -> mapper.taskIdPage(p, q);
        return pu.page(page, taskId);
    }
}
