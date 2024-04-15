package cn.sinozg.applet.turbo.tb.service;

import cn.sinozg.applet.turbo.tb.entity.NodeInstance;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* 节点执行实例表 服务类
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-01-02 21:12:33
*/
public interface NodeInstanceService extends IService<NodeInstance> {

    boolean insertOrUpdateList(List<NodeInstance> list);

    NodeInstance selectRecentOne(String flowInstanceId);
    List<NodeInstance> selectByFlowInstanceId(String flowInstanceId);
    List<NodeInstance> selectDescByFlowInstanceId(String flowInstanceId);

    NodeInstance selectByNodeInstanceId(String flowInstanceId, String nodeInstanceId);

    NodeInstance selectBySourceInstanceId(String flowInstanceId, String sourceNodeInstanceId, String nodeKey);
}
