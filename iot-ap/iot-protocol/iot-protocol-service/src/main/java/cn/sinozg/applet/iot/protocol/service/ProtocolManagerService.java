package cn.sinozg.applet.iot.protocol.service;

import cn.sinozg.applet.iot.protocol.model.ThingServiceParams;
import cn.sinozg.applet.iot.protocol.vo.response.ProtocolModuleRegisterResponse;

import java.util.List;

/**
 * 协议管理 接口
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-16 14:41
 */
public interface ProtocolManagerService {

    /**
     * 初始化
     */
    void init();
    /**
     * 获取到数据库所有的协议信息
     * @return 协议信息
     */
    List<ProtocolModuleRegisterResponse> allProtocolByStateType ();

    /**
     * 注册数据 解析（编码、解码）转化器
     * @param component 协议信息
     * @param protocolMethod 设备协议服务
     * @throws Exception 异常
     */
    void registerAnalysisService (ProtocolModuleRegisterResponse component, ProtocolMethodService protocolMethod) throws Exception;

    /**
     * 注册协议脚本
     * @param id 协议组件id
     * @param type 脚本类型
     * @param protocolMethod 协议服务
     */
    void registerProtocolScript (String id, String type, ProtocolMethodService protocolMethod);

    /**
     * 启动协议 回调处理其他事件
     * 可以多次的重新构造实例 消息处理实例
     * @param protocolMethod 设备协议服务
     */
    void startCallback(ProtocolMethodService protocolMethod);

    /**
     * 注册
     * @param component 协议信息
     */
    void register(ProtocolModuleRegisterResponse component);

    /**
     * 销毁
     * @param id id
     */
    void deRegister(String id);

    /**
     * 启动
     * @param id id
     */
    void start(String id);

    /**
     * 停止
     * @param id id
     */
    void stop(String id);

    /**
     * 是否运行中
     * @param id id
     * @return 是否
     */
    boolean isRunning(String id);

    /**
     * 发送信息
     * @param service 参数
     */
    void send(ThingServiceParams<?> service);

    /**
     * serviceMid
     * @param deviceName 设备名称
     * @param mid mid
     * @return serviceMid
     */
    String getPlatformMid(String deviceName, String mid);
}
