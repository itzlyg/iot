package cn.sinozg.applet.iot.protocol.service.impl;

import cn.sinozg.applet.biz.com.enums.TmIdentifierType;
import cn.sinozg.applet.biz.com.model.TmMessageInfo;
import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.common.utils.SnowFlake;
import cn.sinozg.applet.common.utils.ThreadPool;
import cn.sinozg.applet.iot.common.constant.ProtocolContext;
import cn.sinozg.applet.iot.common.enums.ProtoMessageType;
import cn.sinozg.applet.iot.common.enums.ScriptEnvType;
import cn.sinozg.applet.iot.common.utils.ProtocolUtil;
import cn.sinozg.applet.iot.protocol.model.BaseDeviceInfo;
import cn.sinozg.applet.iot.protocol.model.DeviceAuthInfo;
import cn.sinozg.applet.iot.protocol.model.DeviceMessageInfo;
import cn.sinozg.applet.iot.protocol.model.DeviceRegisterInfo;
import cn.sinozg.applet.iot.protocol.model.DeviceStateInfo;
import cn.sinozg.applet.iot.protocol.model.MessageAction;
import cn.sinozg.applet.iot.protocol.model.MessageOnReceiveResult;
import cn.sinozg.applet.iot.protocol.model.ReceiveMessageInfo;
import cn.sinozg.applet.iot.protocol.service.ProtocolMessageService;
import cn.sinozg.applet.iot.protocol.service.ProtocolMethodService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.function.Consumer;

/**
 * 用来处理协议的接收到的消息
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-11-15 17:59:18
 */
@Slf4j
@Getter
@Setter
public class ProtocolMessageServiceImpl implements ProtocolMessageService {


    private final ProtocolManagerImpl protocolManager;

    private final ProtocolMethodService protocolMethod;

    public ProtocolMessageServiceImpl(ProtocolManagerImpl protocolManager, ProtocolMethodService protocolMethod) {
        this.protocolManager = protocolManager;
        this.protocolMethod = protocolMethod;
    }

    @Override
    public void onReceive(Map<String, Object> headers, ProtoMessageType type, String msg) {
        onReceive(headers, type, msg, r -> {});
    }

    @Override
    public void onReceive(Map<String, Object> headers, ProtoMessageType type, String msg, Consumer<ReceiveMessageInfo> onResult) {
        ThreadPool.submit(() -> {
            try {
                // 消息下发后返回，实际上中应该是ack返回，再通过ack消息类型去解析数据
                MessageOnReceiveResult result = protocolMethod.getProtocolScript()
                        .invokeMethod(MessageOnReceiveResult.class, ProtocolContext.SCRIPT_METHOD_RECEIVE, headers, type.getCode(), msg);
                // 取脚本执行后返回的数据
                if (result == null || StringUtils.isBlank(result.getType()) || result.getData() == null) {
                    onResult.accept(null);
                    return;
                }
                String tp = result.getType();
                if (ProtocolContext.TP_REGISTER.equals(tp)) {
                    this.actionAccept(DeviceRegisterInfo.class, onResult, result, this::doRegister);
                } else if (ProtocolContext.TP_AUTH.equals(tp)) {
                    this.actionAccept(DeviceAuthInfo.class, onResult, result, this::doAuth);
                } else if (ProtocolContext.TP_STATE.equals(tp)) {
                    this.actionAccept(DeviceStateInfo.class, onResult, result, this::doStateChange);
                } else if (ProtocolContext.TP_REPORT.equals(tp)) {
                    this.actionAccept(DeviceMessageInfo.class, onResult, result, this::doReport);
                } else if (ProtocolContext.TP_OTA.equals(tp)) {
                    this.actionAccept(DeviceMessageInfo.class, onResult, result, this::doOta);
                }
            } catch (Throwable e) {
                log.error("receive component message error", e);
            }
            onResult.accept(null);
        });
    }

    private <T extends BaseDeviceInfo> void actionAccept (Class<T> clazz, Consumer<ReceiveMessageInfo> onResult,
                                                      MessageOnReceiveResult result, Consumer<T> consumer){
        T entity = JsonUtil.mapPojo(result.getData(), clazz);
        if (entity == null) {
            onResult.accept(null);
            return;
        }
        consumer.accept(entity);
        MessageAction action = result.getAction();
        if (action == null) {
            return;
        }
        if (ProtocolContext.TYPE_ACK.equals(action.getType())) {
            DeviceMessageInfo deviceMessage = JsonUtil.toPojo(action.getContent(), DeviceMessageInfo.class);
            protocolMethod.send(deviceMessage);
        }
        ReceiveMessageInfo info = new ReceiveMessageInfo();
        info.setData(entity);
        info.setProdKey(entity.getProdKey());
        info.setDeviceCode(entity.getDeviceCode());
        onResult.accept(info);
    }

    /**
     * 注册数据
     * @param info 设备注册信息
     */
    public void doRegister(DeviceRegisterInfo info) {
        try {
            protocolManager.behaviourService.register(info);
        } catch (Throwable e) {
            log.error("register error", e);
        } finally {
            protocolMethod.getProtocolScript().invokeMethod("onRegistered", info, "false");
        }
    }

    /**
     * 设备认证
     * @param auth 设备认证信息
     */
    private void doAuth(DeviceAuthInfo auth) {
        try {
            protocolManager.behaviourService.deviceAuth(auth);
        } catch (Throwable e) {
            log.error("device auth error", e);
        } finally {
            protocolMethod.getProtocolScript().invokeMethod("onAuthed", auth, "false");
        }
    }

    /**
     * 设备状态变更
     * @param state 设备状态信息
     */
    private void doStateChange(DeviceStateInfo state) {
        String pk = state.getProdKey();
        String deviceCode = state.getDeviceCode();
        boolean isOnline = TmIdentifierType.eq(state.getState(), TmIdentifierType.ONLINE);
        try {
            if (isOnline) {
                protocolManager.deviceRouter.putRouter(pk, deviceCode, protocolMethod);
            } else {
                protocolManager.deviceRouter.removeRouter(pk, deviceCode);
            }
            protocolMethod.onDeviceStateChange(state);
            protocolManager.behaviourService.deviceStateChange(pk, deviceCode, isOnline);
        } catch (Throwable e) {
            log.error("device state change error", e);
        }
    }

    /**
     * ota 上报数据
     * @param message 上报数据
     */
    private void doOta(DeviceMessageInfo message) {
        TmMessageInfo tmInfo = protocolMethod.getAnalysisService().decode(message);
        protocolManager.behaviourService.deviceOta(tmInfo);
    }

    /**
     * 上报数据
     * @param message 上报数据
     */
    private void doReport(DeviceMessageInfo message) {
        TmMessageInfo messageInfo = protocolMethod.getAnalysisService().decode(message);
        if (messageInfo == null) {
            return;
        }
        // 服务回复需要重新对应mid
        if (TmIdentifierType.isReply(messageInfo.getIdentifier())) {
            String platformMid = protocolManager.getPlatformMid(messageInfo.getDeviceName(), message.getMid());
            if (platformMid == null) {
                platformMid = SnowFlake.genId();
            }
            messageInfo.setMid(platformMid);
        } else {
            //其它消息重新生成唯一MID
            messageInfo.setMid(ProtocolUtil.requestId());
        }
        protocolManager.behaviourService.reportMessage(messageInfo);
    }


    @Override
    public void putScriptEnv(ScriptEnvType key, Object value) {
        protocolMethod.getProtocolScript().putScriptEnv(key, value);
    }
}
