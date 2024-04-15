package cn.sinozg.applet.turbo.tb.service.impl;

import cn.sinozg.applet.turbo.tb.entity.NodeInstance;
import cn.sinozg.applet.turbo.tb.mapper.NodeInstanceMapper;
import cn.sinozg.applet.turbo.tb.service.NodeInstanceService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* 节点执行实例表 服务实现类
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-01-02 21:12:33
*/
@Slf4j
@Service
public class NodeInstanceServiceImpl extends ServiceImpl<NodeInstanceMapper, NodeInstance> implements NodeInstanceService {

    @Resource
    private NodeInstanceMapper mapper;


    @Override
    public List<NodeInstance> selectByFlowInstanceId(String flowInstanceId) {
        return selectByFlowInstanceId(flowInstanceId, true);
    }

    @Override
    public List<NodeInstance> selectDescByFlowInstanceId(String flowInstanceId) {
        return selectByFlowInstanceId(flowInstanceId, false);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean insertOrUpdateList(List<NodeInstance> list) {
        if (CollectionUtils.isEmpty(list)) {
            log.warn("insertOrUpdateList: nodeInstanceList is empty.");
            return true;
        }
        List<NodeInstance> insertNodeInstanceList = new ArrayList<>();
        list.forEach(nodeInstance -> {
            if (nodeInstance.getId() == null) {
                insertNodeInstanceList.add(nodeInstance);
            } else {
                this.updateStatus(nodeInstance);
            }
        });
        if (CollectionUtils.isEmpty(insertNodeInstanceList)) {
            return true;
        }
        return this.saveBatch(insertNodeInstanceList);

    }

    @Override
    public NodeInstance selectRecentOne(String flowInstanceId) {
        return selectInfo(flowInstanceId, null,  null, null, null);
    }

    @Override
    public NodeInstance selectByNodeInstanceId(String flowInstanceId, String nodeInstanceId) {
        return selectInfo(flowInstanceId, null,  null, null, nodeInstanceId);
    }

    @Override
    public NodeInstance selectBySourceInstanceId(String flowInstanceId, String sourceNodeInstanceId, String nodeKey) {
        return selectInfo(flowInstanceId, null,  sourceNodeInstanceId, nodeKey, null);
    }

    private void updateStatus(NodeInstance nodeInstance){
        this.update(new LambdaUpdateWrapper<NodeInstance>()
                .set(NodeInstance::getStatus, nodeInstance.getStatus())
                .eq(NodeInstance::getId, nodeInstance.getId()));

    }

    private NodeInstance selectInfo (String flowInstanceId, Integer status, String sourceNodeInstanceId, String nodeKey, String nodeInstanceId){
        NodeInstance params = new NodeInstance();
        params.setFlowInstanceId(flowInstanceId);
        params.setStatus(status);
        params.setSourceNodeInstanceId(sourceNodeInstanceId);
        params.setNodeKey(nodeKey);
        params.setNodeInstanceId(nodeInstanceId);
        return mapper.selectInfo(params);
    }

    private List<NodeInstance> selectByFlowInstanceId(String flowInstanceId, boolean asc){
        LambdaQueryWrapper<NodeInstance> wrapper = new LambdaQueryWrapper<NodeInstance>()
                .eq(NodeInstance::getFlowInstanceId, flowInstanceId)
                .orderBy(true, asc, NodeInstance::getId);
        return this.list(wrapper);
    }
}
