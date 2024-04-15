package cn.sinozg.applet.biz.task.mapper;

import cn.sinozg.applet.biz.task.entity.TaskManager;
import cn.sinozg.applet.biz.task.vo.request.TaskManagerPageRequest;
import cn.sinozg.applet.biz.task.vo.response.TaskManagerPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
* 任务管理表 Mapper 接口
* @Description
* @Copyright Copyright (c) 2023
* @author zy
* @since 2023-12-18 22:35:41
*/
public interface TaskManagerMapper extends BaseMapper<TaskManager> {

    /**
    * 根据条件分页查询任务管理表列表
    *
    * @param page 分页信息
    * @param params 任务管理表信息
    * @return 任务管理表信息集合信息
    */
    IPage<TaskManagerPageResponse> pageInfo(Page<TaskManagerPageResponse> page, @Param("p") TaskManagerPageRequest params);

    int callbackStatus(@Param("p") TaskManager params);
}
