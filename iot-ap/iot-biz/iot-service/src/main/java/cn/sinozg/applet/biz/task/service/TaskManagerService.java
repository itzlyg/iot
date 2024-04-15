package cn.sinozg.applet.biz.task.service;

import cn.sinozg.applet.biz.task.entity.TaskManager;
import cn.sinozg.applet.biz.task.vo.request.TaskManagerCreateRequest;
import cn.sinozg.applet.biz.task.vo.request.TaskManagerPageRequest;
import cn.sinozg.applet.biz.task.vo.response.TaskManagerInfoResponse;
import cn.sinozg.applet.biz.task.vo.response.TaskManagerPageResponse;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.PagingRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* 任务管理表 服务类
* @Description
* @Copyright Copyright (c) 2023
* @author zy
* @since 2023-12-18 22:35:41
*/
public interface TaskManagerService extends IService<TaskManager> {

    /**
    * 新增任务管理表
    * @param params 任务管理表
    */
    void createInfo (TaskManagerCreateRequest params);

    /**
    * 查询详情
    * @param id 主键
    * @return 对象
    */
    TaskManagerInfoResponse getInfoById(String id);

    /**
    * 分页查询
    * @param page 分页对象
    * @param params 参数
    * @return 分页集合
    */
    BasePageResponse<List<TaskManagerPageResponse>> pageInfo(PagingRequest page, TaskManagerPageRequest params);
}
