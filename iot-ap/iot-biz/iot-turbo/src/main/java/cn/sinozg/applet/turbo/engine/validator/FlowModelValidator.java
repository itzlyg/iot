package cn.sinozg.applet.turbo.engine.validator;

import cn.sinozg.applet.turbo.engine.common.Constants;
import cn.sinozg.applet.turbo.engine.common.ErrorEnum;
import cn.sinozg.applet.turbo.engine.common.FlowElementType;
import cn.sinozg.applet.turbo.engine.exception.DefinitionException;
import cn.sinozg.applet.turbo.engine.exception.ProcessException;
import cn.sinozg.applet.turbo.engine.model.FlowElement;
import cn.sinozg.applet.turbo.engine.model.FlowModel;
import cn.sinozg.applet.turbo.engine.param.CommonParam;
import cn.sinozg.applet.turbo.engine.util.FlowModelUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FlowModelValidator {

    protected static final Logger LOGGER = LoggerFactory.getLogger(FlowModelValidator.class);

    @Resource
    private ElementValidatorFactory elementValidatorFactory;

    public void validate(FlowModel flowModel) throws ProcessException, DefinitionException {
        this.validate(flowModel, null);
    }

    public void validate(FlowModel flowModel, CommonParam commonParam) throws ProcessException, DefinitionException {
        if (flowModel == null || CollectionUtils.isEmpty(flowModel.getFlowElementList())) {
            LOGGER.warn("message={}", ErrorEnum.MODEL_EMPTY.getErrMsg());
            throw new DefinitionException(ErrorEnum.MODEL_EMPTY);
        }

        List<FlowElement> flowElementList = flowModel.getFlowElementList();
        Map<String, FlowElement> flowElementMap = new HashMap<>(16);

        for (FlowElement flowElement : flowElementList) {
            if (flowElementMap.containsKey(flowElement.getKey())) {
                String elementName = FlowModelUtil.getElementName(flowElement);
                String elementKey = flowElement.getKey();
                String exceptionMsg = MessageFormat.format(Constants.MODEL_DEFINITION_ERROR_MSG_FORMAT,
                    ErrorEnum.ELEMENT_KEY_NOT_UNIQUE, elementName, elementKey);
                LOGGER.warn(exceptionMsg);
                throw new DefinitionException(ErrorEnum.ELEMENT_KEY_NOT_UNIQUE.getErrNo(), exceptionMsg);
            }
            flowElementMap.put(flowElement.getKey(), flowElement);
        }

        int startEventCount = 0;
        int endEventCount = 0;

        for (FlowElement flowElement : flowElementList) {

            ElementValidator elementValidator = elementValidatorFactory.getElementValidator(flowElement);
            elementValidator.validate(flowElementMap, flowElement, commonParam);

            if (FlowElementType.START_EVENT == flowElement.getType()) {
                startEventCount++;
            }

            if (FlowElementType.END_EVENT == flowElement.getType()) {
                endEventCount++;
            }
        }

        if (startEventCount != 1) {
            LOGGER.warn("message={}||startEventCount={}", ErrorEnum.START_NODE_INVALID.getErrMsg(), startEventCount);
            throw new DefinitionException(ErrorEnum.START_NODE_INVALID);
        }

        if (endEventCount < 1) {
            LOGGER.warn("message={}", ErrorEnum.END_NODE_INVALID.getErrMsg());
            throw new DefinitionException(ErrorEnum.END_NODE_INVALID);
        }
    }
}
