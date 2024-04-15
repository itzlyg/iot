package cn.sinozg.applet.turbo.tb.service;

import cn.sinozg.applet.turbo.tb.entity.FlowInstance;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* 流程执行实例表 服务类
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-01-02 21:12:33
*/
public interface FlowInstanceService extends IService<FlowInstance> {

    FlowInstance selectByFlowInstanceId(String flowInstanceId);
    void updateStatus(String flowInstanceId, int status);

    void updateStatus(FlowInstance flowInstance, int status);
}
