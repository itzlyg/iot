package cn.sinozg.applet.iot.protocol.achieve;

import cn.sinozg.applet.common.constant.BaseConstants;
import cn.sinozg.applet.iot.common.utils.ProtocolClassLoaderUtil;
import cn.sinozg.applet.iot.protocol.service.ProtocolManagerService;
import cn.sinozg.applet.iot.protocol.vo.response.ProtocolModuleRegisterResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 设备协议管理器 用来对接到 上层业务服务
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-16 15:48
 */
@Slf4j
@Component
public class DeviceProtocolComponent {

    @Resource
    private ProtocolManagerService protocolBizManager;

    @Resource
    private ProtocolManagerService protocolManager;

    public void register(ProtocolModuleRegisterResponse component) {
        String type = component.getModuleType();
        if (BaseConstants.STATUS_01.equals(type)) {
            protocolManager.register(component);
        } else {
            // 设备
            protocolBizManager.register(component);
        }
    }

    public void deRegister(String id) {
        protocolBizManager.deRegister(id);
        protocolManager.deRegister(id);
        // 手动卸载jar应用，避免重新上传jar被占用
        ProtocolClassLoaderUtil.closeClassLoader(id);
    }

    public void start(String id) {
        protocolBizManager.start(id);
        protocolManager.start(id);
    }

    public void stop(String id) {
        protocolBizManager.stop(id);
        protocolManager.stop(id);
    }

    public boolean isRunning(String id) {
        return protocolBizManager.isRunning(id) || protocolManager.isRunning(id);
    }
}
