package cn.sinozg.applet.iot.protocol.service.impl;


import cn.sinozg.applet.biz.com.model.TmMessageInfo;
import cn.sinozg.applet.common.constant.BaseConstants;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.common.utils.RedisUtil;
import cn.sinozg.applet.iot.common.constant.ProtocolContext;
import cn.sinozg.applet.iot.common.constant.ProtocolRedisKey;
import cn.sinozg.applet.iot.common.enums.ScriptEnvType;
import cn.sinozg.applet.iot.common.utils.ProtocolClassLoaderUtil;
import cn.sinozg.applet.iot.common.utils.ProtocolUtil;
import cn.sinozg.applet.iot.protocol.model.DeviceMessageInfo;
import cn.sinozg.applet.iot.protocol.model.ProtoDeviceInfo;
import cn.sinozg.applet.iot.protocol.model.ThingServiceParams;
import cn.sinozg.applet.iot.protocol.service.BaseProtocolService;
import cn.sinozg.applet.iot.protocol.service.ProtocolMethodService;
import cn.sinozg.applet.iot.protocol.vo.response.DeviceInfoProtocolResponse;
import cn.sinozg.applet.iot.protocol.vo.response.DeviceTagProtocolResponse;
import cn.sinozg.applet.iot.protocol.vo.response.ProductProtocolResponse;
import cn.sinozg.applet.iot.protocol.vo.response.ProtocolAnalysisRegisterResponse;
import cn.sinozg.applet.iot.protocol.vo.response.ProtocolModuleRegisterResponse;
import cn.sinozg.applet.iot.script.factory.ScriptFactory;
import cn.sinozg.applet.iot.script.service.DataAnalysisService;
import cn.sinozg.applet.iot.script.service.ScriptEngineService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备协议管理器
 *
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-15 21:37:53
 */
@Slf4j
@Service(value = ProtocolContext.PROTOCOL_MANAGER)
public class ProtocolManagerImpl extends BaseProtocolManagerService {

    @Override
    public List<ProtocolModuleRegisterResponse> allProtocolByStateType() {
        return protocolByStateType(BaseConstants.STATUS_01, BaseConstants.STATUS_01);
    }

    /**
     * 注册数据转化器
     * @param module 协议信息
     * @param protocolService 设备协议服务
     * @throws Exception 异常
     */
    @Override
    public void registerAnalysisService(ProtocolModuleRegisterResponse module, ProtocolMethodService protocolService) throws Exception {
        DataAnalysisService analysisService;
        if (BaseConstants.STATUS_01.equals(module.getAnalysisType())) {
            // 上传上来的 convert.js
            ProtocolAnalysisRegisterResponse protocolConvert = dataService.analysisInfoById(module.getAnalysisId());
            analysisService = ScriptFactory.scriptConverter(protocolConvert.getType());
            // 从文件方式内容 转换器包下的
            String analysisScript = ProtocolUtil.readScript(module.getAnalysisId(), true);
            analysisService.setAnalyScript(analysisScript);
            analysisService.putScriptEnv(ScriptEnvType.COMPONENT, protocolService);
        } else {
            // convert.spi 获取
            try {
                analysisService = ProtocolClassLoaderUtil.getConverter(module.getId());
            } catch (Throwable e) {
                throw new CavException("获取组件脚本消息错误！", e);
            }
        }
        protocolService.setAnalysisService(analysisService);
    }

    @Override
    public void registerProtocolScript (String id, String type, ProtocolMethodService protocolService) {
        ScriptEngineService protocolScript = ScriptFactory.scriptEngine(type);
        String script = ProtocolUtil.readScript(id, false);
        if (StringUtils.isBlank(script)) {
            log.error("协议脚本信息为空！{},{}", id, type);
        }
        protocolScript.setScript(script);
        protocolService.setProtocolScript(protocolScript);
    }

    @Override
    public void startCallback(ProtocolMethodService protocolService) {
        ProtocolMessageServiceImpl messageService = new ProtocolMessageServiceImpl(this, protocolService);
        messageService.putScriptEnv(ScriptEnvType.BEHAVIOUR, behaviourService);
        messageService.putScriptEnv(ScriptEnvType.COMPONENT, protocolService);
        protocolService.setMessageService(messageService);
    }

    @Override
    public void send(ThingServiceParams<?> service) {
        log.info("开始发送设备消息:{}", JsonUtil.toJson(service));
        if (MapUtils.isEmpty(services)) {
            throw new CavException("协议组件为空！");
        }
        String prodKey = service.getProdKey();
        String deviceCode = service.getDeviceCode();

        DeviceInfoProtocolResponse deviceInfo = dataService.deviceInfoKeyCode(prodKey, service.getDeviceCode());
        String deviceName = deviceInfo.getDeviceName();
        ProductProtocolResponse product = dataService.productInfoByKey(prodKey);

        if (product.isTransparent()) {
            //如果是透传设备，取父级设备进行链路查找
            DeviceInfoProtocolResponse parent = dataService.deviceInfoByCode(deviceInfo.getParentId());
            prodKey = parent.getProdKey();
            deviceCode = parent.getDeviceCode();
            deviceName = parent.getDeviceName();
        }

        BaseProtocolService component = deviceRouter.getRouter(prodKey, deviceCode);
        // 如果缓存里面没有信息
        if (component == null) {
            String comId = dataService.protocolId(prodKey, deviceCode);
            component = services.get(comId);
            deviceRouter.putRouter(prodKey, deviceCode, component);
        }
        if (!(component instanceof ProtocolMethodService)) {
            throw new CavException("发送目标不存在");
        }
        ProtocolMethodService deviceComponent = (ProtocolMethodService) component;

        //构建必要的设备信息
        Map<String, String> tag = new HashMap<>(64);
        Map<String, DeviceTagProtocolResponse> tagMap = deviceInfo.getTag();
        if (MapUtils.isNotEmpty(tagMap)) {
            tagMap.forEach((k, v) -> tag.put(k, v.getValue()));
        }

        ProtoDeviceInfo device = new ProtoDeviceInfo();
        device.setDeviceCode(device.getDeviceCode());
        device.setModel(deviceInfo.getModel());
        device.setProperty(deviceInfo.getProperty());
        device.setTransparent(product.getTransparent());
        device.setTag(tag);

        // 对下发消息进行编码转换
        DataAnalysisService analysisService = deviceComponent.getAnalysisService();
        DeviceMessageInfo message = analysisService.encode(service, device);
        if (message == null) {
            throw new CavException("转化的设备消息为空！");
        }

        String sendMid = message.getMid();
        long timeout = deviceComponent.getConfig().getCmdTimeout();

        //保存设备端mid与平台mid对应关系
        saveMidMapping(message, timeout, service.getMid());
        // 发送消息给设备
        message = deviceComponent.send(message);

        //mid发生改变
        if (!sendMid.equals(message.getMid())) {
            //重新保存消息id映射
            saveMidMapping(message, timeout, service.getMid());
        }
        // 产生下发消息
        TmMessageInfo messageInfo = new TmMessageInfo();
        messageInfo.setMid(service.getMid());
        messageInfo.setProdKey(service.getProdKey());
        messageInfo.setDeviceCode(service.getDeviceCode());
        messageInfo.setIdentifier(service.getIdentifier());
        messageInfo.setType(service.getType());
        messageInfo.setData(service.getParams());
        messageInfo.setDeviceName(deviceName);
        behaviourService.reportMessage(messageInfo);
    }

    @Override
    public String getPlatformMid(String deviceName, String mid) {
        return RedisUtil.getCacheObject(keyMid(deviceName, mid));
    }

    /**
     * 保存设备端mid与平台mid对应关系
     */
    private void saveMidMapping(DeviceMessageInfo message, long cmdTimeout, String serviceMid) {
        RedisUtil.setCacheObject(keyMid(message.getDeviceCode(), message.getMid()), serviceMid, cmdTimeout);
    }


    private String keyMid(String mid, String name) {
        return String.format(ProtocolRedisKey.KEY_CMD_MID, name, mid);
    }
}
