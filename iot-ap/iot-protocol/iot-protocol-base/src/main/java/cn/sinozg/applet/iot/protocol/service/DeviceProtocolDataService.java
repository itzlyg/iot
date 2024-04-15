package cn.sinozg.applet.iot.protocol.service;

import cn.sinozg.applet.biz.com.model.DevicePropertyMappingCache;
import cn.sinozg.applet.biz.com.model.TmMessageInfo;
import cn.sinozg.applet.biz.com.vo.response.TmProtocolResponse;
import cn.sinozg.applet.iot.protocol.model.DeviceConfigInfo;
import cn.sinozg.applet.iot.protocol.vo.response.DeviceInfoProtocolResponse;
import cn.sinozg.applet.iot.protocol.vo.response.DeviceOtaDetailResponse;
import cn.sinozg.applet.iot.protocol.vo.response.ProductModelProtocolResponse;
import cn.sinozg.applet.iot.protocol.vo.response.ProductProtocolResponse;
import cn.sinozg.applet.iot.protocol.vo.response.ProtocolAnalysisRegisterResponse;
import cn.sinozg.applet.iot.protocol.vo.response.ProtocolModuleRegisterResponse;

import java.util.List;
import java.util.Map;

/**
 * 用来获取设备等消息
 * 实现通讯协议解析等接口类
 * 业务服务实现此类，剥离非业务信息
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-16 11:24
 */
public interface DeviceProtocolDataService {

    /**
     * 根据模型型号获取到 模型信息
     * @param model 型号
     * @return 模型信息
     */
    ProductModelProtocolResponse modelInfoByModel (String model);

    /**
     * 通过产品的key 查找产品信息
     * @param prodKey key
     * @return 产品
     */
    ProductProtocolResponse productInfoByKey(String prodKey);

    /**
     * 通过产品key 和设备名称查找设备信息
     * @param prodKey 产品key
     * @param deviceCode 设备code
     * @param notNull 不为null
     * @return 设备信息
     */
    DeviceInfoProtocolResponse deviceInfoKeyCode(String prodKey, String deviceCode, boolean notNull);

    DeviceInfoProtocolResponse deviceInfoKeyCode(String prodKey, String deviceCode);
    /**
     * 保存设备信息
     * @param params 设备信息
     */
    void saveDevice(DeviceInfoProtocolResponse params);

    /**
     * 设备id 查找设备消息
     * @param deviceCode 设备id
     * @return 设备消息
     */
    DeviceInfoProtocolResponse deviceInfoByCode (String deviceCode);

    /**
     * 查找所有的子设备
     * @param deviceCode 设备id
     * @return 子设备
     */
    List<String> findSubDeviceCodes (String deviceCode);

    /**
     * 查询设备ota 详情
     * @param params 查询参数
     * @return 设备消息
     */
    DeviceOtaDetailResponse deviceOtaDetail(DeviceOtaDetailResponse params);

    /**
     * 保存设备ota详情
     * @param params 参数
     */
    void saveDeviceOtaDetail(DeviceOtaDetailResponse params);


    /**
     * 根据状态和类型找到所有的协议组件
     * @param state 状态
     * @param type 类型
     * @return 协议组件
     */
    List<ProtocolModuleRegisterResponse> allProtocolByStateType(String state, String type);

    /**
     * 处理注册信息
     * @param info 信息
     */
    void registerModuleInfo(ProtocolModuleRegisterResponse info);


    /**
     * 获取到转化消息
     * @param id id
     * @return 实体
     */
    ProtocolAnalysisRegisterResponse analysisInfoById(String id);

    /**
     * 设备配置信息
     * @param id 设备id
     * @return 配置信息
     */
    DeviceConfigInfo deviceConfigById(String id);


    /**
     * 物模型信息 协议信息
     * @param prodKey 产品key
     * @return 物模型信息
     */
    TmProtocolResponse tmInfo(String prodKey);

    /**
     * 保存设备配置 到数据库 或者redis
     * @param deviceCode 设备id
     * @param properties 配置信息
     */
    void saveDeviceProperties(String deviceCode, Map<String, DevicePropertyMappingCache> properties);

    /**
     * 保存设备属性 具体记录到时序数据库
     *
     * @param deviceCode   设备ID
     * @param properties 属性 key为属性的key即 field value 含有对应的值和发生时间
     * @param time       属性上报时间
     */
    void addDevicePropertiesRecord(String deviceCode, Map<String, DevicePropertyMappingCache> properties, long time);

    /**
     * 保存设备消息
     * @param msg 消息
     */
    void addModelMessage(TmMessageInfo msg);

    /**
     * 通过产品key 和设备名称 找到协议组件id
     * @param key key
     * @param deviceCode code
     * @return 协议组件id
     */
    String protocolId(String key, String deviceCode);
}
