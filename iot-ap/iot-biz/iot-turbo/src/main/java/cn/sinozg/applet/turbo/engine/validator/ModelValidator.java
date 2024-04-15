package cn.sinozg.applet.turbo.engine.validator;

import cn.sinozg.applet.turbo.engine.common.ErrorEnum;
import cn.sinozg.applet.turbo.engine.exception.DefinitionException;
import cn.sinozg.applet.turbo.engine.exception.ProcessException;
import cn.sinozg.applet.turbo.engine.model.FlowModel;
import cn.sinozg.applet.turbo.engine.param.CommonParam;
import cn.sinozg.applet.turbo.engine.util.FlowModelUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ModelValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelValidator.class);

    @Resource
    private FlowModelValidator flowModelValidator;

    public void validate(String flowModelStr) throws DefinitionException, ProcessException {
        this.validate(flowModelStr, null);
    }

    public void validate(String flowModelStr, CommonParam commonParam) throws DefinitionException, ProcessException {
        if (StringUtils.isBlank(flowModelStr)) {
            LOGGER.warn("message={}", ErrorEnum.MODEL_EMPTY.getErrMsg());
            throw new DefinitionException(ErrorEnum.MODEL_EMPTY);
        }

        FlowModel flowModel = FlowModelUtil.parseModelFromString(flowModelStr);
        if (flowModel == null || CollectionUtils.isEmpty(flowModel.getFlowElementList())) {
            LOGGER.warn("message={}||flowModelStr={}", ErrorEnum.MODEL_EMPTY.getErrMsg(), flowModelStr);
            throw new DefinitionException(ErrorEnum.MODEL_EMPTY);
        }
        flowModelValidator.validate(flowModel, commonParam);
    }
}
