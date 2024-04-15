package cn.sinozg.applet.turbo.tb.mapper;

import cn.sinozg.applet.turbo.tb.entity.FlowDeployment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* 流程部署表 Mapper 接口
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-01-02 21:12:15
*/
public interface FlowDeploymentMapper extends BaseMapper<FlowDeployment> {

    FlowDeployment selectByDeployId(String flowDeployId);

    FlowDeployment selectByModuleId(String flowModuleId);
}
