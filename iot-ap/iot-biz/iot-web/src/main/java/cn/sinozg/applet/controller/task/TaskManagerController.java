package cn.sinozg.applet.controller.task;

import cn.sinozg.applet.biz.task.service.TaskManagerService;
import cn.sinozg.applet.biz.task.vo.request.TaskManagerCreateRequest;
import cn.sinozg.applet.biz.task.vo.request.TaskManagerPageRequest;
import cn.sinozg.applet.biz.task.vo.response.TaskManagerInfoResponse;
import cn.sinozg.applet.biz.task.vo.response.TaskManagerPageResponse;
import cn.sinozg.applet.common.core.base.BasePageRequest;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.BaseRequest;
import cn.sinozg.applet.common.core.base.BaseResponse;
import cn.sinozg.applet.common.core.model.ComId;
import cn.sinozg.applet.common.utils.MsgUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 前端控制器
 * @Description
 * @Copyright Copyright (c) 2023
 * @author zy
 * @since 2023-12-18 22:35:41
 */
@RestController
@RequestMapping("/api/task/task_manager")
@Tag(name = "task-manager-controller", description = "任务管理表接口")
public class TaskManagerController {

    @Resource
    private TaskManagerService service;

    /**
    * 新增记录
    * @param request 请求参数
    * @return 是否成功
    */
    @PostMapping("/add")
    @Operation(summary = "新增任务管理表记录")
    public BaseResponse<String> addTaskManager(@RequestBody @Valid BaseRequest<TaskManagerCreateRequest> request) {
        TaskManagerCreateRequest params = MsgUtil.params(request);
        service.createInfo(params);
        return MsgUtil.ok();
    }

    /**
    * 删除记录
    * @param request 主键
    * @return 是否成功
    */
    @PostMapping("/delete")
    @Operation(summary = "根据主键删除任务管理表记录")
    public BaseResponse<String> deleteTaskManager(@RequestBody @Valid BaseRequest<ComId> request) {
        ComId params = MsgUtil.params(request);
        service.removeById(params.getId());
        return MsgUtil.ok();
    }

    /**
    * 查询详情
    * @param request 主键
    * @return 记录信息
    */
    @PostMapping("/detail")
    @Operation(summary = "查询任务管理表记录")
    public BaseResponse<TaskManagerInfoResponse> detailOfTaskManager(@RequestBody @Valid BaseRequest<ComId> request) {
        ComId params = MsgUtil.params(request);
        TaskManagerInfoResponse entity = service.getInfoById(params.getId());
        return MsgUtil.ok(entity);
    }


    /**
    * 分页查询信息
    * @param request 查询参数
    * @return 分页记录
    */
    @PostMapping("/page_list")
    @Operation(summary = "查询任务管理表分页列表")
    public BasePageResponse<List<TaskManagerPageResponse>> pageOfTaskManager(@RequestBody @Valid BasePageRequest<TaskManagerPageRequest> request) {
        TaskManagerPageRequest params = MsgUtil.params(request);
        return service.pageInfo(request.getPage(), params);
    }
}
