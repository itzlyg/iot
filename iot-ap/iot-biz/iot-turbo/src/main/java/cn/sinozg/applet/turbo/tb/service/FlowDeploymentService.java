package cn.sinozg.applet.turbo.tb.service;

import cn.sinozg.applet.turbo.tb.entity.FlowDeployment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* 流程部署表 服务类
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-01-02 21:12:15
*/
public interface FlowDeploymentService extends IService<FlowDeployment> {

    /**
    * 查询详情
    * @param flowDeployId flowDeployId
    * @return 对象
    */
    FlowDeployment selectByDeployId(String flowDeployId);

    FlowDeployment selectRecentByFlowModuleId(String flowModuleId);
}
