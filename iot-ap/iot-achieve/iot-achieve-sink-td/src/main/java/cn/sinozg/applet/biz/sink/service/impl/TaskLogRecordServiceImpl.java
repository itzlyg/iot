package cn.sinozg.applet.biz.sink.service.impl;

import cn.sinozg.applet.biz.sink.common.TdContext;
import cn.sinozg.applet.biz.sink.common.TdSql;
import cn.sinozg.applet.biz.sink.service.TaskLogRecordService;
import cn.sinozg.applet.biz.sink.util.TdTableUtil;
import cn.sinozg.applet.biz.sink.vo.request.TaskLogRecordSinkRequest;
import cn.sinozg.applet.biz.sink.vo.response.TaskLogRecordPageResponse;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.PagingRequest;
import cn.sinozg.applet.common.utils.PageConvert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-07 15:34
 */
@Slf4j
@Service
public class TaskLogRecordServiceImpl implements TaskLogRecordService {

    @Resource(name = TdContext.JDBC_TEMPLATE)
    private JdbcTemplate template;
    @Override
    public void deleteByTaskId(String taskId) {
        template.update(TdSql.TASK_DEL, taskId);
    }

    @Override
    public BasePageResponse<List<TaskLogRecordPageResponse>> taskIdPage(PagingRequest page, String taskId) {
        List<Long> counts = template.queryForList(TdSql.TASK_PAGE_COUNT, Long.class, taskId);
        long count = TdTableUtil.count(counts);
        List<TaskLogRecordPageResponse> taskLogs = null;
        if (count != 0) {
            String sql = String.format(TdSql.TASK_PAGE_DETAIL, page.getPageSize(), (page.getPageNum() - 1) * page.getPageSize());
            taskLogs = template.query(sql, new BeanPropertyRowMapper<>(TaskLogRecordPageResponse.class), taskId);
        }
        return PageConvert.convert(page, taskLogs, count);
    }

    @Override
    public void add(TaskLogRecordSinkRequest params) {
        String sql = String.format(TdSql.TASK_ADD, TdTableUtil.rightTbName(params.getTaskId()), params.getTaskId());
        template.update(sql, System.currentTimeMillis(), params.getContent(), params.getSuccess());
    }
}
