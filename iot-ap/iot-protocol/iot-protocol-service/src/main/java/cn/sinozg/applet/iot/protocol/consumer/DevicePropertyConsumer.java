package cn.sinozg.applet.iot.protocol.consumer;

import cn.sinozg.applet.biz.com.model.DevicePropertyMappingCache;
import cn.sinozg.applet.biz.com.model.TmBaseDataTypeInfo;
import cn.sinozg.applet.biz.com.model.TmDataTypeInfo;
import cn.sinozg.applet.biz.com.model.TmMessageInfo;
import cn.sinozg.applet.biz.com.vo.response.TmProtocolResponse;
import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.iot.common.enums.ProtocolTopicType;
import cn.sinozg.applet.iot.common.utils.ProtocolUtil;
import cn.sinozg.applet.iot.protocol.service.DeviceProtocolDataService;
import cn.sinozg.applet.iot.protocol.vo.response.DeviceInfoProtocolResponse;
import cn.sinozg.applet.mq.handle.ConsumerAutoHandler;
import cn.sinozg.applet.mq.joint.BaseTopicType;
import cn.sinozg.applet.mq.mq.MqConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备属性消息消费入库
 *
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-16 21:26:31
 */
@Slf4j
@Service
public class DevicePropertyConsumer implements ConsumerAutoHandler<TmMessageInfo> {

    @Resource
    private DeviceProtocolDataService dataService;
    @Resource
    private MqConsumer<TmMessageInfo> consumer;
    @Override
    public BaseTopicType type() {
        return ProtocolTopicType.DEVICE_PROPERTY;
    }

    @Override
    public void initialization() {

    }

    @Override
    public MqConsumer<TmMessageInfo> consumer() {
        return consumer;
    }

    @Override
    public void handler(TmMessageInfo msg) {
        if (!(msg.getData() instanceof Map)) {
            return;
        }
        ProtocolUtil.setTenantId(msg);
        Map<String, Object> properties = PojoUtil.cast(msg.getData());
        String deviceCode = msg.getDeviceCode();
        DeviceInfoProtocolResponse deviceInfo = dataService.deviceInfoByCode(deviceCode);
        if (deviceInfo == null) {
            return;
        }
        // 物模型校验，过滤非物模型属性
        TmProtocolResponse tm = dataService.tmInfo(deviceInfo.getProdKey());
        if (tm == null) {
            return;
        }
        // 物模型属性
        List<TmBaseDataTypeInfo> attributes = tm.getAttributes();
        Map<String, TmDataTypeInfo> tmProperties = PojoUtil.toMap(attributes, TmBaseDataTypeInfo::getIdentifier, TmBaseDataTypeInfo::getDataType);

        Map<String, DevicePropertyMappingCache> addProperties = new HashMap<>(64);
        Long occurred = msg.getOccurred();
        // 删除非属性字段
        for (Map.Entry<String, Object> e : properties.entrySet()) {
            String key = e.getKey();
            if (tmProperties.containsKey(key)) {
                DevicePropertyMappingCache propertyCache = new DevicePropertyMappingCache();
                propertyCache.setValue(e.getValue());
                propertyCache.setOccurred(occurred);
                addProperties.put(key, propertyCache);
                handleLocate(deviceInfo, e.getValue(), tmProperties.get(key));
            }
        }
        // 更新设备当前属性到数据库 redis
        try {
            log.info("保存设备属性, deviceCode:{}, 配置消息为:{}", deviceCode, JsonUtil.toJson(properties));
            dataService.saveDeviceProperties(deviceCode, addProperties);
            // 保存属性记录到数据库
            dataService.addDevicePropertiesRecord(deviceCode, addProperties, occurred);
        } catch (Throwable e) {
            log.error("保存设备当前属性失败！", e);
        }
    }


    private void handleLocate(DeviceInfoProtocolResponse deviceInfo, Object data, TmDataTypeInfo dataType) {
        //如果是定位属性需要做一些处理
        if ("position".equals(dataType.getType())) {
            Object specs = dataType.getSpecs();
            String locateType = "";
            if (specs instanceof Map) {
                //定位方式
                Object objlocateType = ((Map<?, ?>) specs).get("locateType");
                if (objlocateType != null) {
                    locateType = objlocateType.toString();
                }
                if (StringUtils.isNotBlank(locateType)) {
                    //经纬度定位格式：经度,纬度
                    if ("lonLat".equals(locateType)) {
                        String[] lonLats = data.toString().split(",");
                        deviceInfo.getLocate().setLongitude(lonLats[0]);
                        deviceInfo.getLocate().setLatitude(lonLats[1]);
                        dataService.saveDevice(deviceInfo);
                        //基站定位
                    } else if ("basestation".equals(locateType)) {
                        //ip定位
                    } else if ("ipinfo".equals(locateType)) {

                    }
                }
            }
        }
    }
}
