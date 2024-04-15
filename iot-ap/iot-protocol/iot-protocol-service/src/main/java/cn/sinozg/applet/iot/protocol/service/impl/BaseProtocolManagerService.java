package cn.sinozg.applet.iot.protocol.service.impl;


import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.iot.common.utils.ProtocolClassLoaderUtil;
import cn.sinozg.applet.iot.common.utils.ProtocolUtil;
import cn.sinozg.applet.iot.protocol.achieve.DeviceRouter;
import cn.sinozg.applet.iot.protocol.model.ProtocolConfig;
import cn.sinozg.applet.iot.protocol.service.DeviceProtocolDataService;
import cn.sinozg.applet.iot.protocol.service.ProtocolManagerService;
import cn.sinozg.applet.iot.protocol.service.ProtocolMethodService;
import cn.sinozg.applet.iot.protocol.vo.response.ProtocolModuleRegisterResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备协议管理器 基类
 *
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-15 21:37:53
 */
@Slf4j
public abstract class BaseProtocolManagerService implements ProtocolManagerService {

    protected final Map<String, ProtocolMethodService> services = new HashMap<>();
    private final Map<String, Boolean> states = new HashMap<>();

    @Resource
    protected DeviceBehaviourServiceImpl behaviourService;

    @Resource
    protected DeviceProtocolDataService dataService;

    @Resource
    protected DeviceRouter deviceRouter;

    @Override
    public void init() {
        List<ProtocolModuleRegisterResponse> protocolList = allProtocolByStateType();
        if (CollectionUtils.isEmpty(protocolList)) {
            return;
        }
        for (ProtocolModuleRegisterResponse component : protocolList) {
            try {
                register(component);
                start(component.getId());
            } catch (Throwable e) {
                log.error("初始化组件失败！", e);
            }
        }
    }

    /**
     * 注册协议信息
     * @param module 协议配置
     */
    @Override
    public void register(ProtocolModuleRegisterResponse module) {
        String id = module.getId();
        if (services.containsKey(id)) {
            return;
        }
        File file = ProtocolUtil.jarFile(module.getJarId());
        ProtocolMethodService protocolMethod;
        try {
            protocolMethod = ProtocolClassLoaderUtil.getComponent(module.getId(), file);
        } catch (Throwable e) {
            log.error("初始化组件失败，组件id为{}，错误信息{}", module.getId(), e.getMessage());
            throw new CavException("获取通讯组件实例失败！", e);
        }
        ProtocolConfig config = new ProtocolConfig();
        config.setCmdTimeout(300);
        config.setOther(module.getConfig());
        protocolMethod.create(config);
        try {
            // 用来解析其他的事件 和 补充DeviceProtocolService实现方法里面没有实现的逻辑
            // 注册 协议转化器
            registerProtocolScript(id, module.getProtocolScriptType(), protocolMethod);
            // 注册数据转化器
            registerAnalysisService(module, protocolMethod);
            services.put(id, protocolMethod);
            states.put(id, false);
        } catch (Exception e) {
            log.error("获取组件脚本消息错误", e);
            throw new CavException("获取组件脚本消息错误！", e);
        }
        log.info("register over......");
    }

    @Override
    public void deRegister(String id) {
        ProtocolMethodService protocolMethod = services.remove(id);
        states.remove(id);
        if (protocolMethod == null) {
            return;
        }
        protocolMethod.stop();
        protocolMethod.destroy();
    }

    @Override
    public void start(String id) {
        log.info("start {}", id);
        ProtocolMethodService protocolMethod = services.get(id);
        if (protocolMethod == null) {
            log.info("start return.......");
            return;
        }
        startCallback(protocolMethod);
        protocolMethod.start();
        states.put(id, true);
    }

    @Override
    public void stop(String id) {
        ProtocolMethodService protocolMethod = services.get(id);
        if (protocolMethod == null) {
            return;
        }
        protocolMethod.stop();
        states.put(id, false);
    }

    @Override
    public boolean isRunning(String id) {
        return states.containsKey(id) && states.get(id);
    }

    /**
     * 获取协议
     * @param state 状态
     * @param type 类型
     * @return 对象
     */
    protected List<ProtocolModuleRegisterResponse> protocolByStateType(String state, String type){
        return dataService.allProtocolByStateType(state, type);
    }
}
