package cn.sinozg.applet.turbo.engine.validator;

import cn.sinozg.applet.turbo.engine.common.Constants;
import cn.sinozg.applet.turbo.engine.common.ErrorEnum;
import cn.sinozg.applet.turbo.engine.common.FlowElementType;
import cn.sinozg.applet.turbo.engine.config.BusinessConfig;
import cn.sinozg.applet.turbo.engine.exception.DefinitionException;
import cn.sinozg.applet.turbo.engine.model.FlowElement;
import cn.sinozg.applet.turbo.engine.model.FlowModel;
import cn.sinozg.applet.turbo.engine.param.CommonParam;
import cn.sinozg.applet.turbo.engine.util.FlowModelUtil;
import cn.sinozg.applet.turbo.tb.entity.FlowDefinition;
import cn.sinozg.applet.turbo.tb.service.FlowDefinitionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static cn.sinozg.applet.turbo.engine.common.Constants.CallActivityExecuteType.ASYNC;
import static cn.sinozg.applet.turbo.engine.common.Constants.CallActivityExecuteType.SYNC;
import static cn.sinozg.applet.turbo.engine.common.Constants.CallActivityInstanceType.MULTIPLE;
import static cn.sinozg.applet.turbo.engine.common.Constants.CallActivityInstanceType.SINGLE;
import static cn.sinozg.applet.turbo.engine.common.Constants.ElementProperties.CALL_ACTIVITY_EXECUTE_TYPE;
import static cn.sinozg.applet.turbo.engine.common.Constants.ElementProperties.CALL_ACTIVITY_FLOW_MODULE_ID;
import static cn.sinozg.applet.turbo.engine.common.Constants.ElementProperties.CALL_ACTIVITY_INSTANCE_TYPE;

@Service
public class CallActivityValidator extends ElementValidator {

    @Resource
    private BusinessConfig businessConfig;

    @Resource
    private FlowDefinitionService flowDefinitionService;

    @Override
    protected void validate(Map<String, FlowElement> flowElementMap, FlowElement flowElement, CommonParam commonParam) throws DefinitionException {
        checkIncoming(flowElementMap, flowElement);
        checkOutgoing(flowElementMap, flowElement);
        checkProperties(flowElementMap, flowElement);
        checkNestedLevel(flowElementMap, flowElement, commonParam);
    }

    @Override
    protected void checkIncoming(Map<String, FlowElement> flowElementMap, FlowElement flowElement) throws DefinitionException {
        super.checkIncoming(flowElementMap, flowElement);
    }

    @Override
    protected void checkOutgoing(Map<String, FlowElement> flowElementMap, FlowElement flowElement) throws DefinitionException {
        super.checkOutgoing(flowElementMap, flowElement);
        List<String> outgoingList = flowElement.getOutgoing();

        if (outgoingList.size() != 1) {
            throwElementValidatorException(flowElement, ErrorEnum.ELEMENT_TOO_MUCH_OUTGOING);
        }
    }

    protected void checkProperties(Map<String, FlowElement> flowElementMap, FlowElement flowElement) throws DefinitionException {
        Map<String, Object> properties = flowElement.getProperties();
        // 1.check ExecuteType
        if (properties.containsKey(CALL_ACTIVITY_EXECUTE_TYPE)) {
            String value = properties.get(CALL_ACTIVITY_EXECUTE_TYPE).toString();
            if (!(SYNC.equals(value) || ASYNC.equals(value))) {
                throwElementValidatorException(flowElement, ErrorEnum.MODEL_UNKNOWN_ELEMENT_VALUE);
            }
        } else {
            throwElementValidatorException(flowElement, ErrorEnum.REQUIRED_ELEMENT_ATTRIBUTES);
        }
        // 2.check InstanceType
        if (properties.containsKey(CALL_ACTIVITY_INSTANCE_TYPE)) {
            String value = properties.get(CALL_ACTIVITY_INSTANCE_TYPE).toString();
            if (!(SINGLE.equals(value) || MULTIPLE.equals(value))) {
                throwElementValidatorException(flowElement, ErrorEnum.MODEL_UNKNOWN_ELEMENT_VALUE);
            }
        } else {
            throwElementValidatorException(flowElement, ErrorEnum.REQUIRED_ELEMENT_ATTRIBUTES);
        }
        // 3.check data transfer
        Set<String> callActivityParamTypeSet = new TreeSet<>();
        callActivityParamTypeSet.add(Constants.CallActivityParamType.NONE);
        callActivityParamTypeSet.add(Constants.CallActivityParamType.PART);
        callActivityParamTypeSet.add(Constants.CallActivityParamType.FULL);
        // It is allowed not to configure data transfer rules, which is FULL by default
        String callActivityInParamType = (String) properties.getOrDefault(Constants.ElementProperties.CALL_ACTIVITY_IN_PARAM_TYPE, Constants.CallActivityParamType.FULL);
        String callActivityOutParamType = (String) properties.getOrDefault(Constants.ElementProperties.CALL_ACTIVITY_OUT_PARAM_TYPE, Constants.CallActivityParamType.FULL);
        if (!callActivityParamTypeSet.contains(callActivityInParamType)) {
            throwElementValidatorException(flowElement, ErrorEnum.MODEL_UNKNOWN_ELEMENT_VALUE);
        }
        if (!callActivityParamTypeSet.contains(callActivityOutParamType)) {
            throwElementValidatorException(flowElement, ErrorEnum.MODEL_UNKNOWN_ELEMENT_VALUE);
        }
    }

    private void checkNestedLevel(Map<String, FlowElement> flowElementMap, FlowElement flowElement, CommonParam commonParam) throws DefinitionException {
        int callActivityNestedLevel = BusinessConfig.MAX_FLOW_NESTED_LEVEL;
        if (commonParam != null) {
            String caller = commonParam.getCaller();
            callActivityNestedLevel = businessConfig.getCallActivityNestedLevel(caller);
        }
        int nestedLevel = getNestedLevel(flowElement, flowElement, new HashMap<>(16));
        if (callActivityNestedLevel < nestedLevel) {
            throwElementValidatorException(flowElement, ErrorEnum.FLOW_NESTED_LEVEL_EXCEEDED);
        }
    }

    // DFS
    private int getNestedLevel(FlowElement rootFlowElement, FlowElement flowElement, Map<String, Integer> flowModuleId2NestLevelCache) throws DefinitionException {
        if (flowElement.getType() != FlowElementType.CALL_ACTIVITY) {
            return 0;
        }
        Map<String, Object> properties = flowElement.getProperties();
        if (!properties.containsKey(CALL_ACTIVITY_FLOW_MODULE_ID)) {
            return 1; // If no callActivityFlowModuleId is specified, default 1
        }
        String callActivityFlowModuleId = properties.get(CALL_ACTIVITY_FLOW_MODULE_ID).toString();
        if (flowModuleId2NestLevelCache.containsKey(callActivityFlowModuleId)) {
            Integer result = flowModuleId2NestLevelCache.get(callActivityFlowModuleId);
            if (result == BusinessConfig.COMPUTING_FLOW_NESTED_LEVEL) {
                throwElementValidatorException(rootFlowElement, ErrorEnum.FLOW_NESTED_DEAD_LOOP);
            } else {
                return result;
            }
        } else {
            flowModuleId2NestLevelCache.put(callActivityFlowModuleId, BusinessConfig.COMPUTING_FLOW_NESTED_LEVEL);
        }

        FlowDefinition flowDefinition = flowDefinitionService.selectByModuleId(callActivityFlowModuleId);
        if (flowDefinition == null) {
            throwElementValidatorException(rootFlowElement, ErrorEnum.MODEL_UNKNOWN_ELEMENT_VALUE);
        }
        FlowModel flowModel = FlowModelUtil.parseModelFromString(flowDefinition.getFlowModel());
        List<FlowElement> flowElementList = flowModel == null ? new ArrayList<>() : flowModel.getFlowElementList();
        int maxNestedLevel = 0;
        for (FlowElement element : flowElementList) {
            int nestedLevel = getNestedLevel(rootFlowElement, element, flowModuleId2NestLevelCache);
            if (maxNestedLevel < nestedLevel) {
                maxNestedLevel = nestedLevel;
            }
        }
        maxNestedLevel++; // add self
        flowModuleId2NestLevelCache.put(callActivityFlowModuleId, maxNestedLevel);
        return maxNestedLevel;
    }
}
