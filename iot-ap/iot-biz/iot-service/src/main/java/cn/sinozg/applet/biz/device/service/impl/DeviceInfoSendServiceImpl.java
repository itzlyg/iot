package cn.sinozg.applet.biz.device.service.impl;

import cn.sinozg.applet.biz.com.enums.TmIdentifierType;
import cn.sinozg.applet.biz.com.enums.TmType;
import cn.sinozg.applet.biz.com.model.TmBaseDataTypeInfo;
import cn.sinozg.applet.biz.com.model.TmDataTypeInfo;
import cn.sinozg.applet.biz.com.model.TmServiceOutInfo;
import cn.sinozg.applet.biz.com.vo.response.TmProtocolResponse;
import cn.sinozg.applet.biz.device.entity.DeviceInfo;
import cn.sinozg.applet.biz.device.mapper.DeviceInfoMapper;
import cn.sinozg.applet.biz.device.service.DeviceInfoSendService;
import cn.sinozg.applet.biz.product.service.ProdInfoService;
import cn.sinozg.applet.common.constant.RedisKey;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.common.utils.RedisUtil;
import cn.sinozg.applet.iot.common.utils.ProtocolUtil;
import cn.sinozg.applet.iot.protocol.model.DeviceBaseInfo;
import cn.sinozg.applet.iot.protocol.model.DeviceConfigInfo;
import cn.sinozg.applet.iot.protocol.model.ThingServiceParams;
import cn.sinozg.applet.iot.protocol.service.DeviceProtocolDataService;
import cn.sinozg.applet.iot.protocol.service.ProtocolManagerService;
import cn.sinozg.applet.iot.protocol.vo.response.DeviceInfoProtocolResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发送设备指令
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-15 18:21
 */
@Slf4j
@Service
public class DeviceInfoSendServiceImpl implements DeviceInfoSendService {

    @Resource
    private ProdInfoService prodInfoService;

    @Resource
    private ProtocolManagerService protocolManager;

    @Resource
    private DeviceInfoMapper deviceInfoMapper;

    @Resource
    private DeviceProtocolDataService dataService;

    /**
     *
     * @param deviceId 设备id
     * @param sendName sendName
     * @param identifier 类型
     * @param args 参数
     * @return 结果
     */
    @Override
    public String invokeService(String deviceId, String sendName, String identifier, Map<String, Object> args) {
        return send(deviceId, sendName, TmType.SERVICE, identifier, args);
    }

    /**
     *
     * @param deviceId 设备id
     * @param sendName sendName
     * @param data 参数
     * @return 结果
     */
    @Override
    public String otaUpgrade(String deviceId, String sendName, Object data) {
        return send(deviceId, sendName, TmType.OTA, TmIdentifierType.OTA.getEnName(), data);
    }


    /**
     *
     * @param deviceId 设备id
     * @param sendName sendName
     * @param properties 参数
     * @return 结果
     */
    @Override
    public String getProperty(String deviceId, String sendName, List<String> properties) {
        return send(deviceId, sendName, TmType.PROPERTY, TmIdentifierType.GET.getEnName(), properties);
    }

    /**
     * 设备属性设置
     * @param deviceId 设备id
     * @param sendName sendName
     * @param properties 参数配置
     * @return 结果
     */
    @Override
    public String setProperty(String deviceId, String sendName, Map<String, Object> properties) {
        return send(deviceId, sendName, TmType.PROPERTY, TmIdentifierType.SET.getEnName(), properties);
    }

    @Override
    public String reportProperty(String deviceId, String sendName, Map<String, Object> properties) {
        return send(deviceId, sendName, TmType.PROPERTY, TmIdentifierType.REPORT.getEnName(), properties);
    }

    /**
     * 设备配置下发
     * @param deviceId 设备id
     * @param sendName sendName
     * @return 结果
     */
    @Override
    public String sendConfig(String deviceId, String sendName) {
        DeviceConfigInfo config = dataService.deviceConfigById(deviceId);
        Map<String, ?> data = JsonUtil.toMap(config.getConfig());
        return send(deviceId, sendName, TmType.CONFIG, TmIdentifierType.SET.getEnName(), data);
    }


    /**
     * 解绑子设备
     * @param deviceId 设备id
     * @param sendName sendName
     */
    @Override
    public void unbindDevice(String deviceId, String sendName) {
        DeviceInfo device = infoById(deviceId);
        DeviceInfoProtocolResponse parent = dataService.deviceInfoByCode(device.getDeviceCode());
        DeviceBaseInfo deviceBase = new DeviceBaseInfo();
        deviceBase.setProdKey(device.getProdKey());
        deviceBase.setDeviceCode(device.getDeviceCode());
        try {
            // 下发子设备注销给网关
            send(parent.getDeviceCode(), sendName, parent.getProdKey(), TmType.LIFE_TIME, TmIdentifierType.DEREGISTER.getEnName(), deviceBase);
        } catch (Throwable e) {
            log.error("解绑子设备失败！", e);
        }
        // 清除设备的父级id，不管是否发送成功都需要清除父级id
        // device.setParentId("");
        // deviceInfoData.save(device);
    }

    @Override
    public void sendTask(String deviceId, String sendName, Object o) {
        String mid = send(deviceId, sendName, TmType.SERVICE, TmIdentifierType.REPORT.getEnName(), o);
        log.info("发送后返回的结果为：{}", mid);
    }

    @Override
    public void sendAck(String deviceId, Object o) {
        String mid = send(deviceId, "ack回复", TmType.SERVICE, TmIdentifierType.ACK.getEnName(), o);
        log.info("发送后返回的结果为：{}", mid);
    }

    private DeviceInfo infoById(String deviceId){
        DeviceInfo info = deviceInfoMapper.selectById(deviceId);
        if (info == null) {
            throw new CavException("设备未找到！");
        }
        return info;
    }

    private String send(String deviceId, String sendName, TmType type, String identifier, Object data){
        DeviceInfo info = infoById(deviceId);
        return send(info.getDeviceCode(), sendName, info.getProdKey(), type, identifier, data);
    }

    private String send(String deviceCode, String sendName, String prodKey, TmType type, String identifier, Object data) {
        ThingServiceParams<Object> params = new ThingServiceParams<>();
        String mid = ProtocolUtil.requestId();
        params.setMid(mid);
        params.setDeviceCode(deviceCode);
        params.setProdKey(prodKey);
        params.setType(type);
        params.setParams(data);
        params.setIdentifier(identifier);
        // 非配置非OTA且非生命周期下发需要做物模型转换
        if (type != TmType.CONFIG && type != TmType.LIFE_TIME && type != TmType.OTA) {
            parseParams(params);
        }
        RedisUtil.setCacheObject(String.format(RedisKey.MID_NAME, mid), sendName, Duration.ofDays(30));
        // 设备指令下发
        protocolManager.send(params);
        return params.getMid();
    }

    private void parseParams(ThingServiceParams<Object> service) {
        TmProtocolResponse tmInfo = prodInfoService.tmInfo(service.getProdKey());
        Object params = null;
        String type = service.getType();
        // 属性设置
        if (TmType.eq(type, TmType.PROPERTY)) {
            List<TmBaseDataTypeInfo> attributesInfos = tmInfo.getAttributes();
            if (attributesInfos == null) {
                return;
            }
            if(TmIdentifierType.eq(service.getIdentifier(), TmIdentifierType.GET)){
                params = service.getParams();
            } else {
                params = parseMap(attributesInfos, service.getParams());
            }
        } else if (TmType.eq(type, TmType.SERVICE)) {
            // 服务调用
            List<TmServiceOutInfo> services = tmInfo.getService();
            TmServiceOutInfo tmServiceInfo = null;
            if (CollectionUtils.isNotEmpty(services)) {
                for (TmServiceOutInfo s : services) {
                    if (service.getIdentifier().equals(s.getIdentifier())) {
                        tmServiceInfo = s;
                        break;
                    }
                }
            }
            if (tmServiceInfo == null) {
                return;
            }
            params = parseMap(tmServiceInfo.getInputData(), service.getParams());
        }
        service.setParams(params);
    }

    private static Map<String, Object> parseMap(List<TmBaseDataTypeInfo> list, Object params){
        Map<String, Object> parsed = new HashMap<>(64);
        if (params instanceof Map) {
            Map<?, ?> paramsMap = PojoUtil.cast(params);
            for (TmBaseDataTypeInfo s : list) {
                parseField(s, paramsMap, parsed);
            }
        }
        return parsed;
    }

    private static void parseField(TmBaseDataTypeInfo dataTypeInfo, Map<?, ?> params, Map<String, Object> parsed) {
        Object val = params.get(dataTypeInfo.getIdentifier());
        if (val == null) {
            return;
        }
        Object result = parse(dataTypeInfo.getDataType(), val);
        if (result == null) {
            return;
        }
        parsed.put(dataTypeInfo.getIdentifier(), result);
    }

    private static <T> Object parse(TmDataTypeInfo dataType, T value) {
        if (value == null) {
            return null;
        }
        String val = value.toString();
        String type = dataType.getType();
        switch (type) {
            case "bool":
            case "enum":
            case "int":
                return Integer.parseInt(val);
            default:
                return val;
        }

    }
}
