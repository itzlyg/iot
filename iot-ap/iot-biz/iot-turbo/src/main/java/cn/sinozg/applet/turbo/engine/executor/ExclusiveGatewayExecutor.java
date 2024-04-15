package cn.sinozg.applet.turbo.engine.executor;

import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.common.utils.SpringUtil;
import cn.sinozg.applet.turbo.engine.bo.NodeInstanceBo;
import cn.sinozg.applet.turbo.engine.common.FlowElementType;
import cn.sinozg.applet.turbo.engine.common.InstanceDataType;
import cn.sinozg.applet.turbo.engine.common.NodeInstanceStatus;
import cn.sinozg.applet.turbo.engine.common.RuntimeContext;
import cn.sinozg.applet.turbo.engine.exception.ProcessException;
import cn.sinozg.applet.turbo.engine.model.FlowElement;
import cn.sinozg.applet.turbo.engine.model.InstanceDataVo;
import cn.sinozg.applet.turbo.engine.spi.HookService;
import cn.sinozg.applet.turbo.engine.util.FlowModelUtil;
import cn.sinozg.applet.turbo.engine.util.InstanceDataUtil;
import cn.sinozg.applet.turbo.tb.entity.InstanceData;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service(value = FlowElementType.NM_EXCLUSIVE_GATEWAY)
public class ExclusiveGatewayExecutor extends AbstractElementExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExclusiveGatewayExecutor.class);

    private List<HookService> hookServices;

    /**
     * Update data map: invoke hook service to update data map
     * You can implement HookService and all implementations of 'HookService' will be executed.
     * Param: one of flowElement's properties
     */
    @Override
    protected void doExecute(RuntimeContext runtimeContext) throws ProcessException {
        // 1.get hook param
        FlowElement flowElement = runtimeContext.getCurrentNodeModel();
        String hookInfoParam = FlowModelUtil.getHookInfos(flowElement);

        // 2.ignore while properties is empty
        if (StringUtils.isBlank(hookInfoParam)) {
            return;
        }

        // 3.invoke hook and get data result
        Map<String, InstanceDataVo> hookInfoValueMap = getHookInfoValueMap(runtimeContext.getFlowInstanceId(), hookInfoParam, runtimeContext.getCurrentNodeInstance().getNodeKey(), runtimeContext.getCurrentNodeInstance().getNodeInstanceId());
        LOGGER.info("doExecute getHookInfoValueMap.||hookInfoValueMap={}", hookInfoValueMap);
        if (MapUtils.isEmpty(hookInfoValueMap)) {
            LOGGER.warn("doExecute: hookInfoValueMap is empty.||flowInstanceId={}||hookInfoParam={}||nodeKey={}",
                runtimeContext.getFlowInstanceId(), hookInfoParam, flowElement.getKey());
            return;
        }

        // 4.merge data to current dataMap
        Map<String, InstanceDataVo> dataMap = runtimeContext.getInstanceDataMap();
        dataMap.putAll(hookInfoValueMap);

        // 5.save data
        if (MapUtils.isNotEmpty(dataMap)) {
            String instanceDataId = saveInstanceDataPo(runtimeContext);
            runtimeContext.setInstanceDataId(instanceDataId);
        }
    }

    private Map<String, InstanceDataVo> getHookInfoValueMap(String flowInstanceId, String hookInfoParam, String nodeKey, String nodeInstanceId) {
        List<InstanceDataVo> dataList = new ArrayList<>();
        ensureHookService();
        for (HookService service : hookServices) {
            try {
                List<InstanceDataVo> list = service.invoke(flowInstanceId, hookInfoParam, nodeKey, nodeInstanceId);
                if (CollectionUtils.isEmpty(list)) {
                    LOGGER.warn("hook service invoke result is empty, serviceName={}, flowInstanceId={}, hookInfoParam={}",
                        service.getClass().getName(), flowInstanceId, hookInfoParam);
                }
                dataList.addAll(list);
            } catch (Exception e) {
                LOGGER.warn("hook service invoke fail, serviceName={}, flowInstanceId={}, hookInfoParam={}",
                    service.getClass().getName(), flowInstanceId, hookInfoParam);
            }
        }
        return InstanceDataUtil.getInstanceDataMap(dataList);
    }

    private String saveInstanceDataPo(RuntimeContext runtimeContext) {
        String instanceDataId = genId();
        InstanceData instanceData = buildHookInstanceData(instanceDataId, runtimeContext);
        instanceDataService.save(instanceData);
        return instanceDataId;
    }

    private InstanceData buildHookInstanceData(String instanceDataId, RuntimeContext runtimeContext) {
        InstanceData instanceData = PojoUtil.copyBean(runtimeContext, InstanceData.class);
        instanceData.setInstanceDataId(instanceDataId);
        instanceData.setInstanceData(InstanceDataUtil.getInstanceDataListStr(runtimeContext.getInstanceDataMap()));
        instanceData.setNodeInstanceId(runtimeContext.getCurrentNodeInstance().getNodeInstanceId());
        instanceData.setNodeKey(runtimeContext.getCurrentNodeModel().getKey());
        instanceData.setType(InstanceDataType.HOOK);
        return instanceData;
    }

    @Override
    protected void postExecute(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstanceBo currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        currentNodeInstance.setInstanceDataId(runtimeContext.getInstanceDataId());
        currentNodeInstance.setStatus(NodeInstanceStatus.COMPLETED);
        runtimeContext.getNodeInstanceList().add(currentNodeInstance);
    }

    /**
     * Calculate unique outgoing
     * Expression: one of flowElement's properties
     * Input: data map
     *
     * @return
     * @throws Exception
     */
    @Override
    protected AbstractRuntimeExecutor getExecuteExecutor(RuntimeContext runtimeContext) throws ProcessException {
        FlowElement nextNode = calculateNextNode(runtimeContext.getCurrentNodeModel(),
            runtimeContext.getFlowElementMap(), runtimeContext.getInstanceDataMap());

        runtimeContext.setCurrentNodeModel(nextNode);
        return executorFactory.getElementExecutor(nextNode);
    }

    private void ensureHookService() {
        if (hookServices != null) {
            return;
        }
        // init hook services by Spring application context
        synchronized (ExclusiveGatewayExecutor.class) {
            if (hookServices != null) {
                return;
            }
            Map<String, HookService> map = SpringUtil.beansOfType(HookService.class);
            hookServices = new ArrayList<>();
            map.forEach((k, v) -> hookServices.add(v));
        }
    }
}
