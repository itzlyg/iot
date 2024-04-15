package cn.sinozg.applet.iot.protocol.service;

import cn.sinozg.applet.iot.protocol.model.DeviceAuthInfo;
import cn.sinozg.applet.iot.protocol.model.DeviceRegisterInfo;
import cn.sinozg.applet.iot.protocol.model.DeviceStateInfo;
import cn.sinozg.applet.iot.protocol.model.DeviceMessageInfo;
import cn.sinozg.applet.iot.script.service.DataAnalysisService;
import cn.sinozg.applet.iot.script.service.ScriptEngineService;

/**
 * 设备协议 服务
 * 相关基础方法 接口
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-15 16:54
 */
public interface ProtocolMethodService extends BaseProtocolService {

    /**
     * 设备授权
     * @param authInfo 授权信息
     */
    void onDeviceAuth(DeviceAuthInfo authInfo);

    /**
     * 设备注册
     * @param info 注册信息
     */
    void onDeviceRegister(DeviceRegisterInfo info);

    /**
     * 设备状态改变
     * @param state 状态信息
     */
    void onDeviceStateChange(DeviceStateInfo state);

    /**
     * 发送消息
     * @param message 消息
     * @return 返回消息
     */
    DeviceMessageInfo send(DeviceMessageInfo message);

    /**
     * 注入消息发送器
     * @param messageService 消息发送器
     */
    void setMessageService(ProtocolMessageService messageService);

    /**
     * 消息发送器
     * @return 消息发送器
     */
    ProtocolMessageService getMessageService();

    /**
     * 注入数据解析器
     * @param analysisService 数据解析器
     */
    void setAnalysisService(DataAnalysisService analysisService);

    /**
     * 得到数据解析器
     * @return 数据解析器
     */
    DataAnalysisService getAnalysisService();


    /**
     * 注入协议脚本解析器
     * @param engine  协议脚本解析器
     */
    void setProtocolScript(ScriptEngineService engine);

    /**
     * 等到协议脚本解析器
     * @return 协议脚本解析器
     */
    ScriptEngineService getProtocolScript();
}
