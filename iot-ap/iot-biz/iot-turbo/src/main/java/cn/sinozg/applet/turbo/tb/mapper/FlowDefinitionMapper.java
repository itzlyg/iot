package cn.sinozg.applet.turbo.tb.mapper;

import cn.sinozg.applet.turbo.tb.entity.FlowDefinition;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* 流程定义表 Mapper 接口
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-01-02 21:12:15
*/
public interface FlowDefinitionMapper extends BaseMapper<FlowDefinition> {

    FlowDefinition selectByFlowModuleId (String flowModuleId);
}
