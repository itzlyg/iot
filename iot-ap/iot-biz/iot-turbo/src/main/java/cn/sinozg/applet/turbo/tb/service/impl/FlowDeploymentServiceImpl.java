package cn.sinozg.applet.turbo.tb.service.impl;

import cn.sinozg.applet.turbo.tb.entity.FlowDeployment;
import cn.sinozg.applet.turbo.tb.mapper.FlowDeploymentMapper;
import cn.sinozg.applet.turbo.tb.service.FlowDeploymentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* 流程部署表 服务实现类
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-01-02 21:12:15
*/
@Service
public class FlowDeploymentServiceImpl extends ServiceImpl<FlowDeploymentMapper, FlowDeployment> implements FlowDeploymentService {

    @Resource
    private FlowDeploymentMapper mapper;


    @Override
    public FlowDeployment selectByDeployId(String flowDeployId) {
        return mapper.selectByDeployId(flowDeployId);
    }

    @Override
    public FlowDeployment selectRecentByFlowModuleId(String flowModuleId) {
        return mapper.selectByModuleId(flowModuleId);
    }
}
