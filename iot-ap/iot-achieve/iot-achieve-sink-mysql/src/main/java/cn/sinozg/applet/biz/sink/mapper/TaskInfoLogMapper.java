package cn.sinozg.applet.biz.sink.mapper;

import cn.sinozg.applet.biz.sink.entity.TaskInfoLog;
import cn.sinozg.applet.biz.sink.vo.response.TaskLogRecordPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
* 任务日志 Mapper 接口
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-03-07 15:35:53
*/
public interface TaskInfoLogMapper extends BaseMapper<TaskInfoLog> {
    IPage<TaskLogRecordPageResponse> taskIdPage(Page<TaskLogRecordPageResponse> page, @Param("taskId") String params);
}
