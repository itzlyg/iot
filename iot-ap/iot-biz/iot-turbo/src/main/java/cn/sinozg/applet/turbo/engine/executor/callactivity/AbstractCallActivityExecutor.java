package cn.sinozg.applet.turbo.engine.executor.callactivity;

import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.turbo.engine.bo.DataTransferBo;
import cn.sinozg.applet.turbo.engine.common.Constants;
import cn.sinozg.applet.turbo.engine.common.ErrorEnum;
import cn.sinozg.applet.turbo.engine.common.InstanceDataType;
import cn.sinozg.applet.turbo.engine.common.RuntimeContext;
import cn.sinozg.applet.turbo.engine.config.BusinessConfig;
import cn.sinozg.applet.turbo.engine.exception.ProcessException;
import cn.sinozg.applet.turbo.engine.executor.AbstractElementExecutor;
import cn.sinozg.applet.turbo.engine.model.FlowElement;
import cn.sinozg.applet.turbo.engine.model.InstanceDataVo;
import cn.sinozg.applet.turbo.engine.processor.RuntimeProcessor;
import cn.sinozg.applet.turbo.engine.service.NodeInstanceDataService;
import cn.sinozg.applet.turbo.engine.util.InstanceDataUtil;
import cn.sinozg.applet.turbo.tb.entity.InstanceData;
import cn.sinozg.applet.turbo.tb.service.FlowDeploymentService;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Some common CallActivity methods
 */
public abstract class AbstractCallActivityExecutor extends AbstractElementExecutor {

    @Resource
    protected RuntimeProcessor runtimeProcessor;

    @Resource
    protected FlowDeploymentService flowDeploymentService;

    @Resource
    protected NodeInstanceDataService nodeInstanceDataService;

    @Resource
    protected BusinessConfig businessConfig;

    protected List<InstanceDataVo> getCallActivityVariables(RuntimeContext runtimeContext) throws ProcessException {
        List<InstanceDataVo> callActivityInitData = InstanceDataUtil.getInstanceDataList(runtimeContext.getInstanceDataMap());
        List<InstanceDataVo> instanceDataFromMainFlow = calculateCallActivityInParamFromMainFlow(runtimeContext);
        // merge data
        Map<String, InstanceDataVo> callActivityInitDataMap = InstanceDataUtil.getInstanceDataMap(callActivityInitData);
        Map<String, InstanceDataVo> instanceDataFromMainFlowMap = InstanceDataUtil.getInstanceDataMap(instanceDataFromMainFlow);

        Map<String, InstanceDataVo> allInstanceData = new HashMap<>(16);
        allInstanceData.putAll(callActivityInitDataMap);
        allInstanceData.putAll(instanceDataFromMainFlowMap);
        return InstanceDataUtil.getInstanceDataList(allInstanceData);
    }

    // main > sub
    protected List<InstanceDataVo> calculateCallActivityInParamFromMainFlow(RuntimeContext runtimeContext) throws ProcessException {
        FlowElement currentNodeModel = runtimeContext.getCurrentNodeModel();

        InstanceData instanceData = instanceDataService.selectByIns(runtimeContext.getFlowInstanceId(), runtimeContext.getInstanceDataId());
        Map<String, InstanceDataVo> mainInstanceDataMap = InstanceDataUtil.getInstanceDataMap(instanceData.getInstanceData());

        return calculateCallActivityDataTransfer(currentNodeModel, mainInstanceDataMap,
            Constants.ElementProperties.CALL_ACTIVITY_IN_PARAM_TYPE,
            Constants.ElementProperties.CALL_ACTIVITY_IN_PARAM);
    }

    // sub > main
    protected List<InstanceDataVo> calculateCallActivityOutParamFromSubFlow(RuntimeContext runtimeContext, List<InstanceDataVo> subFlowData) throws ProcessException {
        FlowElement currentNodeModel = runtimeContext.getCurrentNodeModel();
        return calculateCallActivityDataTransfer(currentNodeModel, InstanceDataUtil.getInstanceDataMap(subFlowData),
            Constants.ElementProperties.CALL_ACTIVITY_OUT_PARAM_TYPE,
            Constants.ElementProperties.CALL_ACTIVITY_OUT_PARAM);
    }

    private List<InstanceDataVo> calculateCallActivityDataTransfer(FlowElement currentNodeModel, Map<String, InstanceDataVo> instanceDataMap, String callActivityParamType, String callActivityParam) throws ProcessException {
        // default FULL
        String callActivityInParamType = (String) currentNodeModel.getProperties().getOrDefault(callActivityParamType, Constants.CallActivityParamType.FULL);
        if (callActivityInParamType.equals(Constants.CallActivityParamType.NONE)) {
            return new ArrayList<>();
        }
        if (callActivityInParamType.equals(Constants.CallActivityParamType.PART)) {
            List<InstanceDataVo> instanceDataList = new ArrayList<>();
            String callActivityInParam = (String) currentNodeModel.getProperties().getOrDefault(callActivityParam, StringUtils.EMPTY);
            List<DataTransferBo> callActivityDataTransfers = JsonUtil.toPojos(callActivityInParam, DataTransferBo.class);

            assert callActivityDataTransfers != null;
            for (DataTransferBo callActivityDataTransfer : callActivityDataTransfers) {
                if (Constants.CallActivityDataTransferType.SOURCE_TYPE_CONTEXT.equals(callActivityDataTransfer.getSourceType())) {
                    InstanceDataVo sourceInstanceData = instanceDataMap.get(callActivityDataTransfer.getSourceKey());
                    Object sourceValue = sourceInstanceData == null ? null : sourceInstanceData.getValue();
                    InstanceDataVo instanceData = new InstanceDataVo(callActivityDataTransfer.getTargetKey(), sourceValue);
                    instanceDataList.add(instanceData);
                } else if (Constants.CallActivityDataTransferType.SOURCE_TYPE_FIXED.equals(callActivityDataTransfer.getSourceType())) {
                    InstanceDataVo instanceData = new InstanceDataVo(callActivityDataTransfer.getTargetKey(), callActivityDataTransfer.getSourceValue());
                    instanceDataList.add(instanceData);
                } else {
                    throw new ProcessException(ErrorEnum.MODEL_UNKNOWN_ELEMENT_VALUE);
                }
            }
            return instanceDataList;
        }
        if (callActivityInParamType.equals(Constants.CallActivityParamType.FULL)) {
            return InstanceDataUtil.getInstanceDataList(instanceDataMap);
        }
        throw new ProcessException(ErrorEnum.MODEL_UNKNOWN_ELEMENT_VALUE);
    }

    protected InstanceData buildCallActivityEndInstanceData(String instanceDataId, RuntimeContext runtimeContext) {
        InstanceData instanceData = PojoUtil.copyBean(runtimeContext, InstanceData.class);
        instanceData.setInstanceDataId(instanceDataId);
        instanceData.setInstanceData(InstanceDataUtil.getInstanceDataListStr(runtimeContext.getInstanceDataMap()));
        instanceData.setNodeInstanceId(runtimeContext.getCurrentNodeInstance().getNodeInstanceId());
        instanceData.setNodeKey(runtimeContext.getCurrentNodeModel().getKey());
        instanceData.setType(InstanceDataType.UPDATE);
        return instanceData;
    }
}
