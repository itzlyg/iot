package cn.sinozg.applet.biz.task.service.impl;

import cn.sinozg.applet.biz.device.service.DeviceInfoSendService;
import cn.sinozg.applet.biz.task.entity.TaskManager;
import cn.sinozg.applet.biz.task.mapper.TaskManagerMapper;
import cn.sinozg.applet.biz.task.service.TaskManagerService;
import cn.sinozg.applet.biz.task.vo.request.TaskManagerCreateRequest;
import cn.sinozg.applet.biz.task.vo.request.TaskManagerPageRequest;
import cn.sinozg.applet.biz.task.vo.request.param.TaskFunctionParamRequest;
import cn.sinozg.applet.biz.task.vo.response.TaskManagerInfoResponse;
import cn.sinozg.applet.biz.task.vo.response.TaskManagerPageResponse;
import cn.sinozg.applet.common.constant.Constants;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.PagingRequest;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.PageUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.iot.common.utils.ProtocolUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* 任务管理表 服务实现类
* @Description
* @Copyright Copyright (c) 2023
* @author zy
* @since 2023-12-18 22:35:41
*/
@Service
public class TaskManagerServiceImpl extends ServiceImpl<TaskManagerMapper, TaskManager> implements TaskManagerService {

    @Resource
    private TaskManagerMapper mapper;

    @Resource
    private DeviceInfoSendService deviceInfoSendService;

    @Override
    public void createInfo(TaskManagerCreateRequest params) {
        TaskManager entity = PojoUtil.copyBean(params, TaskManager.class);
        String mid = ProtocolUtil.requestId();
        entity.setTaskTransactionId(mid);
        entity.setExecuteStatus(Constants.STATUS_00);
        entity.setExecuteTime(LocalDateTime.now());
        this.save(entity);
        Map<String, Object> paramsMap = new HashMap<>(64);
        for (TaskFunctionParamRequest p : params.getExecutionParams()) {
            paramsMap.put(p.getParameterIdentifier(), p.getValue());
        }
        deviceInfoSendService.invokeService(params.getDeviceId(), params.getTaskName(), params.getFunctionId(), paramsMap);
    }

    @Override
    public TaskManagerInfoResponse getInfoById(String id) {
        TaskManager entity = this.infoById(id);
        return PojoUtil.copyBean(entity, TaskManagerInfoResponse.class);
    }

    @Override
    public BasePageResponse<List<TaskManagerPageResponse>> pageInfo(PagingRequest page, TaskManagerPageRequest params) {
        PageUtil<TaskManagerPageResponse, TaskManagerPageRequest> pu = (p, q) -> mapper.pageInfo(p, q);
        return pu.page(page, params);
    }

    /**
     * 查询详情
     * @param id 主键id
     * @return 实体对象
     */
    private TaskManager infoById(String id){
        TaskManager entity = this.getById(id);
        if (entity == null) {
            throw new CavException("未找到任务管理表！");
        }
        return entity;
    }
}
