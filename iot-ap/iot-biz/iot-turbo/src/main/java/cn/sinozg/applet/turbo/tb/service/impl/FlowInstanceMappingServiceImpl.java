package cn.sinozg.applet.turbo.tb.service.impl;

import cn.sinozg.applet.turbo.tb.entity.FlowInstanceMapping;
import cn.sinozg.applet.turbo.tb.mapper.FlowInstanceMappingMapper;
import cn.sinozg.applet.turbo.tb.service.FlowInstanceMappingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

;

/**
* 父子流程实例映射表 服务实现类
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-01-02 21:12:33
*/
@Service
public class FlowInstanceMappingServiceImpl extends ServiceImpl<FlowInstanceMappingMapper, FlowInstanceMapping> implements FlowInstanceMappingService {

    @Resource
    private FlowInstanceMappingMapper mapper;


    @Override
    public List<FlowInstanceMapping> selectFlowInstanceMappingList(String flowInstanceId, String nodeInstanceId) {
        FlowInstanceMapping params = new FlowInstanceMapping();
        params.setNodeInstanceId(nodeInstanceId);
        params.setFlowInstanceId(flowInstanceId);
        return mapper.selectFlowInstanceMappingList(params);
    }

    @Override
    public FlowInstanceMapping selectFlowInstanceMapping(String flowInstanceId, String nodeInstanceId) {
        List<FlowInstanceMapping> list = selectFlowInstanceMappingList(flowInstanceId, nodeInstanceId);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public int updateType(String flowInstanceId, String nodeInstanceId, int type) {
        FlowInstanceMapping params = new FlowInstanceMapping();
        params.setType(type);
        params.setFlowInstanceId(flowInstanceId);
        params.setNodeInstanceId(nodeInstanceId);
        return mapper.updateType(params);
    }
}
