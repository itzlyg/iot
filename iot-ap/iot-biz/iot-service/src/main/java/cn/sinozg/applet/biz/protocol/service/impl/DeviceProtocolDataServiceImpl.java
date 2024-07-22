package cn.sinozg.applet.biz.protocol.service.impl;

import cn.sinozg.applet.biz.com.enums.TmIdentifierType;
import cn.sinozg.applet.biz.com.model.DevicePropertyMappingCache;
import cn.sinozg.applet.biz.com.model.TmMessageInfo;
import cn.sinozg.applet.biz.com.vo.response.TmProtocolResponse;
import cn.sinozg.applet.biz.common.cache.DeviceCache;
import cn.sinozg.applet.biz.common.menus.WebSocketType;
import cn.sinozg.applet.biz.device.service.DeviceInfoService;
import cn.sinozg.applet.biz.device.vo.request.DeviceInfoProtocolRequest;
import cn.sinozg.applet.biz.product.entity.ProdInfo;
import cn.sinozg.applet.biz.product.service.ProdInfoService;
import cn.sinozg.applet.biz.protocol.mapper.ProtocolModuleMapper;
import cn.sinozg.applet.biz.protocol.service.AnalysisScriptService;
import cn.sinozg.applet.biz.protocol.service.ProtocolConfigService;
import cn.sinozg.applet.biz.protocol.vo.module.BaseProtocolConfig;
import cn.sinozg.applet.biz.protocol.vo.request.ProtocolModuleStateTypeRequest;
import cn.sinozg.applet.biz.sink.service.DevicePropertySinkService;
import cn.sinozg.applet.biz.sink.service.TmMsgSinkService;
import cn.sinozg.applet.biz.sink.vo.request.PropertyInfoAddRequest;
import cn.sinozg.applet.biz.sink.vo.request.TmSinkAddRequest;
import cn.sinozg.applet.biz.task.entity.TaskManager;
import cn.sinozg.applet.biz.task.mapper.TaskManagerMapper;
import cn.sinozg.applet.common.constant.BaseConstants;
import cn.sinozg.applet.common.constant.Constants;
import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.iot.protocol.model.DeviceConfigInfo;
import cn.sinozg.applet.iot.protocol.model.DeviceMessageInfo;
import cn.sinozg.applet.iot.protocol.service.DeviceProtocolDataService;
import cn.sinozg.applet.iot.protocol.vo.response.DeviceInfoProtocolResponse;
import cn.sinozg.applet.iot.protocol.vo.response.DeviceOtaDetailResponse;
import cn.sinozg.applet.iot.protocol.vo.response.ProductModelProtocolResponse;
import cn.sinozg.applet.iot.protocol.vo.response.ProductProtocolResponse;
import cn.sinozg.applet.iot.protocol.vo.response.ProtocolAnalysisRegisterResponse;
import cn.sinozg.applet.iot.protocol.vo.response.ProtocolModuleRegisterResponse;
import cn.sinozg.applet.ws.handler.WebsocketPublish;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于设备协议通讯 与上层业务之间数据的传递
 *
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-16 16:04
 */
@Slf4j
@Service
public class DeviceProtocolDataServiceImpl implements DeviceProtocolDataService {

    @Resource
    private DeviceCache deviceCache;

    @Resource
    private ProtocolModuleMapper moduleMapper;

    @Resource
    private ProtocolConfigService configService;

    @Resource
    private AnalysisScriptService analysisScriptService;

    @Resource
    private ProdInfoService prodInfoService;

    @Resource
    private DeviceInfoService deviceInfoService;

    @Resource
    private TmMsgSinkService tmSinkService;

    @Resource
    private DevicePropertySinkService devicePropertySinkService;

    @Resource
    private TaskManagerMapper taskManagerMapper;

    @Resource
    private WebsocketPublish websocketPublish;

    @Override
    public ProductModelProtocolResponse modelInfoByModel(String model) {
        return null;
    }

    @Override
    public ProductProtocolResponse productInfoByKey(String key) {
        ProdInfo prod = prodInfoService.infoByIdOrKey(null, key);
        ProductProtocolResponse product = new ProductProtocolResponse();
        product.setProdKey(key);
        product.setName(prod.getName());
        product.setClassifyId(prod.getClassifyId());
        product.setUid(prod.getCreatedBy());
        product.setTransparent(false);
        return product;
    }

    @Override
    public DeviceInfoProtocolResponse deviceInfoKeyCode(String prodKey, String deviceCode, boolean notNull) {
        DeviceInfoProtocolRequest params = new DeviceInfoProtocolRequest();
        params.setProdKey(prodKey);
        params.setDeviceCode(deviceCode);
        params.setNotNull(notNull);
        return deviceInfoService.deviceInfoForProtocol(params);
    }

    @Override
    public DeviceInfoProtocolResponse deviceInfoKeyCode(String prodKey, String deviceCode) {
        return deviceInfoKeyCode(prodKey, deviceCode, true);
    }


    @Override
    public void saveDevice(DeviceInfoProtocolResponse params) {
        deviceInfoService.saveDeviceInfoByProtocol(params);
    }

    @Override
    public DeviceInfoProtocolResponse deviceInfoByCode(String deviceCode) {
        DeviceInfoProtocolRequest params = new DeviceInfoProtocolRequest();
        params.setDeviceCode(deviceCode);
        return deviceInfoService.deviceInfoForProtocol(params);
    }

    @Override
    public List<String> findSubDeviceCodes(String deviceCode) {
        return null;
    }

    @Override
    public DeviceOtaDetailResponse deviceOtaDetail(DeviceOtaDetailResponse params) {
        return null;
    }

    @Override
    public void saveDeviceOtaDetail(DeviceOtaDetailResponse params) {

    }

    @Override
    public List<ProtocolModuleRegisterResponse> allProtocolByStateType(String status, String type) {
        ProtocolModuleStateTypeRequest params = new ProtocolModuleStateTypeRequest();
        params.setModuleType(type);
        params.setDataStatus(status);
        List<ProtocolModuleRegisterResponse> list = moduleMapper.protocolModuleStateType(params);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(this::registerModuleInfo);
            list.sort(Comparator.comparing(ProtocolModuleRegisterResponse::getMode).reversed());
        }
        return list;
    }

    @Override
    public void registerModuleInfo(ProtocolModuleRegisterResponse info){
        BaseProtocolConfig config = configService.getInfoByProtocolId(info.getId(), info.getProtocolType());
        info.setConfig(JsonUtil.toJson(config));
        String mode = JsonUtil.key2Val(info.getConfig(), "mode");
        if (StringUtils.isBlank(mode)) {
            mode = BaseConstants.STATUS_00;
        }
        info.setMode(mode);
    }

    @Override
    public ProtocolAnalysisRegisterResponse analysisInfoById(String id) {
        return analysisScriptService.analysisRegister(id);
    }

    @Override
    public DeviceConfigInfo deviceConfigById(String id) {
        DeviceConfigInfo config = new DeviceConfigInfo();
        config.setConfig("{\n" +
                "\"name\":\"zll\"\n" +
                "}");
        return config;
    }

    @Override
    public TmProtocolResponse tmInfo(String prodKey) {
        return prodInfoService.tmInfo(prodKey);
    }

    @Override
    public void saveDeviceProperties(String deviceCode, Map<String, DevicePropertyMappingCache> properties) {
        deviceCache.setDevicePropertyCache(deviceCode, properties);
    }

    @Override
    public void addDevicePropertiesRecord(String deviceCode, Map<String, DevicePropertyMappingCache> properties, long time) {
        DeviceInfoProtocolRequest query = new DeviceInfoProtocolRequest();
        query.setDeviceCode(deviceCode);
        DeviceInfoProtocolResponse device = deviceInfoService.deviceInfoForProtocol(query);
        if (device == null) {
            return;
        }
        // 获取设备旧属性
        Map<String, DevicePropertyMappingCache> all = deviceCache.devicePropertyCache(deviceCode);
        // 用新属性覆盖
        all.putAll(properties);
        PropertyInfoAddRequest addParams = new PropertyInfoAddRequest();
        addParams.setDeviceCode(deviceCode);
        addParams.setProdKey(device.getProdKey());
        addParams.setTime(time);

        addParams.setDetails(all);
        devicePropertySinkService.addProperties(addParams);
        Map<String, String> data = new HashMap<>(16);
        data.put("id", device.getId());
        websocketPublish.push(WebSocketType.DEVICE_PROPERTIES, data, device.getId());
    }

    @Override
    public void addModelMessage(TmMessageInfo msg) {
        TmSinkAddRequest params = PojoUtil.copyBean(msg, TmSinkAddRequest.class);
        boolean isReply = TmIdentifierType.isReply(msg.getIdentifier());
        TaskManager update = new TaskManager();
        update.setTaskTransactionId(params.getMid());
        params.setTs(msg.getTime());
        update.setExecuteStatus(isReply ? Constants.STATUS_03 : Constants.STATUS_02);
        taskManagerMapper.callbackStatus(update);
        tmSinkService.add(params);
    }

    @Override
    public String protocolId(String prodKey, String deviceCode) {
        DeviceInfoProtocolRequest params = new DeviceInfoProtocolRequest();
        params.setProdKey(prodKey);
        params.setDeviceCode(deviceCode);
        return deviceInfoService.protocolIdForDevice(params);
    }

    @Override
    public void sendCallback(DeviceMessageInfo device, TmMessageInfo msg) {

    }

    @Override
    public void recordCallback(TmMessageInfo msg) {

    }
}
