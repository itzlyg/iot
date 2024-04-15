package cn.sinozg.applet.turbo.tb.service.impl;

import cn.sinozg.applet.turbo.tb.entity.FlowInstance;
import cn.sinozg.applet.turbo.tb.mapper.FlowInstanceMapper;
import cn.sinozg.applet.turbo.tb.service.FlowInstanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* 流程执行实例表 服务实现类
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-01-02 21:12:33
*/
@Service
public class FlowInstanceServiceImpl extends ServiceImpl<FlowInstanceMapper, FlowInstance> implements FlowInstanceService {

    @Resource
    private FlowInstanceMapper mapper;


    @Override
    public FlowInstance selectByFlowInstanceId(String flowInstanceId) {
        return mapper.selectByFlowInstanceId(flowInstanceId);
    }

    @Override
    public void updateStatus(String flowInstanceId, int status) {
        FlowInstance flowInstance = selectByFlowInstanceId(flowInstanceId);
        updateStatus(flowInstance, status);
    }

    @Override
    public void updateStatus(FlowInstance flowInstance, int status) {
        flowInstance.setStatus(status);
        baseMapper.updateStatus(flowInstance);
    }
}
