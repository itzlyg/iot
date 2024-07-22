package cn.sinozg.applet.iot.protocol.service.impl;


import cn.sinozg.applet.common.constant.BaseConstants;
import cn.sinozg.applet.iot.common.constant.ProtocolContext;
import cn.sinozg.applet.iot.common.enums.ScriptEnvType;
import cn.sinozg.applet.iot.protocol.model.ThingServiceParams;
import cn.sinozg.applet.iot.protocol.service.ProtocolMethodService;
import cn.sinozg.applet.iot.protocol.vo.response.ProtocolModuleRegisterResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 其他业务协议管理
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-11-16 15:09:09
 */
@Slf4j
@Service(value = ProtocolContext.PROTOCOL_BIZ_MANAGER)
public class ProtocolBizManagerImpl extends BaseProtocolManagerService {


    @Override
    public List<ProtocolModuleRegisterResponse> allProtocolByStateType() {
        return protocolByStateType(BaseConstants.STATUS_01, BaseConstants.STATUS_02);
    }

    @Override
    public void registerAnalysisService(ProtocolModuleRegisterResponse component, ProtocolMethodService protocolService) throws Exception {
        protocolService.putScriptEnv(ScriptEnvType.BEHAVIOUR, behaviourService);
        // protocolService.putScriptEnv("apiTool", new ApiTool());
    }

    @Override
    public void registerProtocolScript (String id, String type, ProtocolMethodService protocolService) {

    }

    @Override
    public void startCallback(ProtocolMethodService protocolService) {

    }

    @Override
    public void send(ThingServiceParams<?> service) {

    }

    @Override
    public String getPlatformMid(String deviceName, String mid) {
        return null;
    }
}
