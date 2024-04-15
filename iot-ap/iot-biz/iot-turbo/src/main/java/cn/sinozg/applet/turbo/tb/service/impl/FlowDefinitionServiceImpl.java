package cn.sinozg.applet.turbo.tb.service.impl;

import cn.sinozg.applet.turbo.tb.entity.FlowDefinition;
import cn.sinozg.applet.turbo.tb.mapper.FlowDefinitionMapper;
import cn.sinozg.applet.turbo.tb.service.FlowDefinitionService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* 流程定义表 服务实现类
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-01-02 21:12:15
*/
@Slf4j
@Service
public class FlowDefinitionServiceImpl extends ServiceImpl<FlowDefinitionMapper, FlowDefinition> implements FlowDefinitionService {

    @Resource
    private FlowDefinitionMapper mapper;


    @Override
    public int updateInfo(FlowDefinition params) {
        if (null == params) {
            log.warn("updateByModuleId failed: flowDefinitionPO is null.");
            return -1;
        }
        try {
            String flowModuleId = params.getFlowModuleId();
            if (StringUtils.isBlank(flowModuleId)) {
                log.warn("updateByModuleId failed: flowModuleId is empty.||flowDefinitionPO={}", params);
                return -1;
            }
            LambdaUpdateWrapper<FlowDefinition> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(FlowDefinition::getFlowModuleId, flowModuleId);
            return mapper.update(params, updateWrapper);
        } catch (Exception e) {
            log.error("update exception.||flowDefinitionPO={}", params, e);
        }
        return -1;
    }

    @Override
    public FlowDefinition selectByModuleId(String flowModuleId) {
        if (StringUtils.isBlank(flowModuleId)) {
            log.warn("getById failed: flowModuleId is empty.");
            return null;
        }
        try {
            return mapper.selectByFlowModuleId(flowModuleId);
        } catch (Exception e) {
            log.error("getById exception.||flowModuleId={}", flowModuleId, e);
        }
        return null;
    }
}
