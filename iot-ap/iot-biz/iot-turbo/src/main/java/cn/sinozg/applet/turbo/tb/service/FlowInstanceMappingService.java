package cn.sinozg.applet.turbo.tb.service;

import cn.sinozg.applet.turbo.tb.entity.FlowInstanceMapping;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* 父子流程实例映射表 服务类
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-01-02 21:12:33
*/
public interface FlowInstanceMappingService extends IService<FlowInstanceMapping> {

    List<FlowInstanceMapping> selectFlowInstanceMappingList (String flowInstanceId, String nodeInstanceId);

    FlowInstanceMapping selectFlowInstanceMapping(String flowInstanceId, String nodeInstanceId);

    int updateType(String flowInstanceId, String nodeInstanceId, int type);
}
