package cn.sinozg.applet.turbo.engine.processor;

import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.common.utils.SnowFlake;
import cn.sinozg.applet.turbo.engine.common.ErrorEnum;
import cn.sinozg.applet.turbo.engine.common.FlowDefinitionStatus;
import cn.sinozg.applet.turbo.engine.common.FlowDeploymentStatus;
import cn.sinozg.applet.turbo.engine.common.FlowModuleEnum;
import cn.sinozg.applet.turbo.engine.exception.DefinitionException;
import cn.sinozg.applet.turbo.engine.exception.ParamException;
import cn.sinozg.applet.turbo.engine.exception.TurboException;
import cn.sinozg.applet.turbo.engine.param.CreateFlowParam;
import cn.sinozg.applet.turbo.engine.param.DeployFlowParam;
import cn.sinozg.applet.turbo.engine.param.GetFlowModuleParam;
import cn.sinozg.applet.turbo.engine.param.UpdateFlowParam;
import cn.sinozg.applet.turbo.engine.result.CommonResult;
import cn.sinozg.applet.turbo.engine.result.CreateFlowResult;
import cn.sinozg.applet.turbo.engine.result.DeployFlowResult;
import cn.sinozg.applet.turbo.engine.result.FlowModuleResult;
import cn.sinozg.applet.turbo.engine.result.UpdateFlowResult;
import cn.sinozg.applet.turbo.engine.validator.ModelValidator;
import cn.sinozg.applet.turbo.engine.validator.ParamValidator;
import cn.sinozg.applet.turbo.tb.entity.FlowDefinition;
import cn.sinozg.applet.turbo.tb.entity.FlowDeployment;
import cn.sinozg.applet.turbo.tb.service.FlowDefinitionService;
import cn.sinozg.applet.turbo.tb.service.FlowDeploymentService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DefinitionProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefinitionProcessor.class);

    @Resource
    private ModelValidator modelValidator;

    @Resource
    private FlowDefinitionService flowDefinitionService;

    @Resource
    protected FlowDeploymentService flowDeploymentService;

    public CreateFlowResult create(CreateFlowParam createFlowParam) {
        CreateFlowResult createFlowResult = new CreateFlowResult();
        try {
            ParamValidator.validate(createFlowParam);

            FlowDefinition flowDefinition = PojoUtil.copyBean(createFlowParam, FlowDefinition.class);
            String flowModuleId = SnowFlake.genId();
            flowDefinition.setFlowModuleId(flowModuleId);
            flowDefinition.setStatus(FlowDefinitionStatus.INIT);

            boolean rows = flowDefinitionService.save(flowDefinition);
            if (!rows) {
                LOGGER.warn("create flow failed: insert to db failed.||createFlowParam={}", createFlowParam);
                throw new DefinitionException(ErrorEnum.DEFINITION_INSERT_INVALID);
            }

            PojoUtil.copyBean(flowDefinition, createFlowResult);
            fillCommonResult(createFlowResult, ErrorEnum.SUCCESS);
        } catch (TurboException te) {
            fillCommonResult(createFlowResult, te);
        }
        return createFlowResult;
    }

    public UpdateFlowResult update(UpdateFlowParam updateFlowParam) {
        UpdateFlowResult updateFlowResult = new UpdateFlowResult();
        try {
            ParamValidator.validate(updateFlowParam);

            FlowDefinition flowDefinition = PojoUtil.copyBean(updateFlowParam, FlowDefinition.class);
            flowDefinition.setStatus(FlowDefinitionStatus.EDITING);

            int rows = flowDefinitionService.updateInfo(flowDefinition);
            if (rows != 1) {
                LOGGER.warn("update flow failed: update to db failed.||updateFlowParam={}", updateFlowParam);
                throw new DefinitionException(ErrorEnum.DEFINITION_UPDATE_INVALID);
            }
            fillCommonResult(updateFlowResult, ErrorEnum.SUCCESS);
        } catch (TurboException te) {
            fillCommonResult(updateFlowResult, te);
        }
        return updateFlowResult;
    }

    public DeployFlowResult deploy(DeployFlowParam deployFlowParam) {
        DeployFlowResult deployFlowResult = new DeployFlowResult();
        try {
            ParamValidator.validate(deployFlowParam);

            FlowDefinition flowDefinition = flowDefinitionService.selectByModuleId(deployFlowParam.getFlowModuleId());
            if (null == flowDefinition) {
                LOGGER.warn("deploy flow failed: flow is not exist.||deployFlowParam={}", deployFlowParam);
                throw new DefinitionException(ErrorEnum.FLOW_NOT_EXIST);
            }

            Integer status = flowDefinition.getStatus();
            if (status != FlowDefinitionStatus.EDITING) {
                LOGGER.warn("deploy flow failed: flow is not editing status.||deployFlowParam={}", deployFlowParam);
                throw new DefinitionException(ErrorEnum.FLOW_NOT_EDITING);
            }

            String flowModel = flowDefinition.getFlowModel();
            modelValidator.validate(flowModel, deployFlowParam);

            FlowDeployment flowDeployment = PojoUtil.copyBean(flowDefinition, FlowDeployment.class);
            String flowDeployId = SnowFlake.genId();
            flowDeployment.setFlowDeployId(flowDeployId);
            flowDeployment.setStatus(FlowDeploymentStatus.DEPLOYED);

            boolean rows = flowDeploymentService.save(flowDeployment);
            if (!rows) {
                LOGGER.warn("deploy flow failed: insert to db failed.||deployFlowParam={}", deployFlowParam);
                throw new DefinitionException(ErrorEnum.DEFINITION_INSERT_INVALID);
            }

            PojoUtil.copyBean(flowDeployment, deployFlowResult);

            fillCommonResult(deployFlowResult, ErrorEnum.SUCCESS);
        } catch (TurboException te) {
            fillCommonResult(deployFlowResult, te);
        }
        return deployFlowResult;
    }

    public FlowModuleResult getFlowModule(GetFlowModuleParam getFlowModuleParam) {
        FlowModuleResult flowModuleResult = new FlowModuleResult();
        try {
            ParamValidator.validate(getFlowModuleParam);
            String flowModuleId = getFlowModuleParam.getFlowModuleId();
            String flowDeployId = getFlowModuleParam.getFlowDeployId();
            if (StringUtils.isNotBlank(flowDeployId)) {
                flowModuleResult = getFlowModuleByFlowDeployId(flowDeployId);
            } else {
                flowModuleResult = getFlowModuleByFlowModuleId(flowModuleId);
            }
            fillCommonResult(flowModuleResult, ErrorEnum.SUCCESS);
        } catch (TurboException te) {
            fillCommonResult(flowModuleResult, te);
        }
        return flowModuleResult;
    }

    private FlowModuleResult getFlowModuleByFlowModuleId(String flowModuleId) throws ParamException {
        FlowDefinition flowDefinition = flowDefinitionService.selectByModuleId(flowModuleId);
        if (flowDefinition == null) {
            LOGGER.warn("getFlowModuleByFlowModuleId failed: can not find flowDefinitionPO.||flowModuleId={}", flowModuleId);
            throw new ParamException(ErrorEnum.PARAM_INVALID.getErrNo(), "flowDefinitionPO is not exist");
        }
        FlowModuleResult flowModuleResult = PojoUtil.copyBean(flowDefinition, FlowModuleResult.class);
        Integer status = FlowModuleEnum.getStatusByDefinitionStatus(flowDefinition.getStatus());
        flowModuleResult.setStatus(status);
        LOGGER.info("getFlowModuleByFlowModuleId||flowModuleId={}||FlowModuleResult={}", flowModuleId, JsonUtil.toJson(flowModuleResult));
        return flowModuleResult;
    }

    private FlowModuleResult getFlowModuleByFlowDeployId(String flowDeployId) throws ParamException {
        FlowDeployment flowDeployment = flowDeploymentService.selectByDeployId(flowDeployId);
        if (flowDeployment == null) {
            LOGGER.warn("getFlowModuleByFlowDeployId failed: can not find flowDefinitionPO.||flowDeployId={}", flowDeployId);
            throw new ParamException(ErrorEnum.PARAM_INVALID.getErrNo(), "flowDefinitionPO is not exist");
        }
        FlowModuleResult flowModuleResult = PojoUtil.copyBean(flowDeployment, FlowModuleResult.class);
        Integer status = FlowModuleEnum.getStatusByDeploymentStatus(flowDeployment.getStatus());
        flowModuleResult.setStatus(status);
        LOGGER.info("getFlowModuleByFlowDeployId||flowDeployId={}||response={}", flowDeployId, JsonUtil.toJson(flowModuleResult));
        return flowModuleResult;
    }

    private void fillCommonResult(CommonResult commonResult, ErrorEnum errorEnum) {
        fillCommonResult(commonResult, errorEnum.getErrNo(), errorEnum.getErrMsg());
    }

    private void fillCommonResult(CommonResult commonResult, TurboException turboException) {
        fillCommonResult(commonResult, turboException.getErrNo(), turboException.getErrMsg());
    }

    private void fillCommonResult(CommonResult commonResult, int errNo, String errMsg) {
        commonResult.setErrCode(errNo);
        commonResult.setErrMsg(errMsg);
    }
}
