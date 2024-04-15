package cn.sinozg.applet.biz.sink.service.impl;

import cn.sinozg.applet.biz.com.model.DevicePropertyMappingCache;
import cn.sinozg.applet.biz.sink.entity.PropertyLog;
import cn.sinozg.applet.biz.sink.service.DevicePropertySinkService;
import cn.sinozg.applet.biz.sink.service.PropertyLogService;
import cn.sinozg.applet.biz.sink.vo.request.DevicePropertyHistoryRequest;
import cn.sinozg.applet.biz.sink.vo.request.PropertyInfoAddRequest;
import cn.sinozg.applet.biz.sink.vo.response.PropertyInfoHistoryResponse;
import cn.sinozg.applet.common.constant.RedisKey;
import cn.sinozg.applet.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-05 20:27
 */
@Slf4j
@Service
public class DevicePropertySinkServiceImpl implements DevicePropertySinkService {

    @Resource
    private PropertyLogService propertyLogService;

    @Override
    public List<PropertyInfoHistoryResponse> devicePropertyHistory(DevicePropertyHistoryRequest params) {
        return propertyLogService.devicePropertyHistory(params);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void addProperties(PropertyInfoAddRequest params) {
        PropertyLog record = new PropertyLog();
        record.setDeviceCode(params.getDeviceCode());
        record.setProdKey(params.getProdKey());
        record.setTs(params.getTime());
        Map<String, Object> jsonMap = new HashMap<>(16);
        Map<String, DevicePropertyMappingCache> details = params.getDetails();
        if (MapUtils.isNotEmpty(details)) {
            for (Map.Entry<String, DevicePropertyMappingCache> e : details.entrySet()) {
                jsonMap.put(e.getKey(), e.getValue().getValue());
            }
        }
        record.setRecordJson(jsonMap);
        propertyLogService.save(record);
        RedisUtil.setCacheObject(String.format(RedisKey.DEVICE_PROPERTY_TB, params.getDeviceCode()), true);
    }

    @Override
    public Map<String, Object> lastRecord(String deviceCode) {
        Boolean ex = RedisUtil.getCacheObject(String.format(RedisKey.DEVICE_PROPERTY_TB, deviceCode));
        if (ex != null && ex) {
            PropertyLog record = propertyLogService.lastRecord(deviceCode);
            if (record != null) {
                return record.getRecordJson();
            }
        }
        return new HashMap<>(16);
    }
}
