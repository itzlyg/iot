package cn.sinozg.applet.turbo.tb.service;

import cn.sinozg.applet.turbo.tb.entity.FlowDefinition;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* 流程定义表 服务类
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-01-02 21:12:15
*/
public interface FlowDefinitionService extends IService<FlowDefinition> {


    /**
    * 修改流程定义表
    * @param params 流程定义表
    */
    int updateInfo(FlowDefinition params);

    FlowDefinition selectByModuleId(String flowModuleId);
}
