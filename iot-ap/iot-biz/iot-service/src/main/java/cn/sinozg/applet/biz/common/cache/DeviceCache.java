package cn.sinozg.applet.biz.common.cache;

import cn.sinozg.applet.biz.com.model.DevicePropertyMappingCache;
import cn.sinozg.applet.biz.sink.service.DevicePropertySinkService;
import cn.sinozg.applet.common.constant.RedisKey;
import cn.sinozg.applet.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-08 20:21
 */
@Slf4j
@Component
public class DeviceCache {

    @Resource
    private DevicePropertySinkService propertySinkService;

    /**
     * 设施设备属性缓存
     * @param deviceCode 设备code
     * @param map 数据
     */
    public void setDevicePropertyCache (String deviceCode, Map<String, DevicePropertyMappingCache> map){
        String key = devicePropertyKey(deviceCode);
        Map<String, DevicePropertyMappingCache> all = RedisUtil.getCacheMap(key);
        if (all == null) {
            all = new HashMap<>(64);
        }
        all.putAll(map);
        RedisUtil.setCacheMap(key, all);
    }

    /**
     * 获取到设备属性缓存
     * @param deviceCode 设备code
     * @return 缓存
     */
    public Map<String, DevicePropertyMappingCache> devicePropertyCache(String deviceCode){
        String key = devicePropertyKey(deviceCode);
        Map<String, DevicePropertyMappingCache> map = RedisUtil.getCacheMap(key);
        // query
        if (MapUtils.isEmpty(map)) {
            Map<String, Object> value = propertySinkService.lastRecord(deviceCode);
            if (map == null) {
                map = new HashMap<>(64);
            }
            if (MapUtils.isNotEmpty(value)) {
                for (Map.Entry<String, Object> e : value.entrySet()) {
                    DevicePropertyMappingCache c = new DevicePropertyMappingCache();
                    c.setValue(e.getValue());
                    map.put(e.getKey(), c);
                }
            }
            setDevicePropertyCache(deviceCode, map);
        }
        return map;
    }


    /**
     * 设备属性缓存key
     * @param deviceCode 设备code
     * @return key
     */
    private String devicePropertyKey (String deviceCode){
        return String.format(RedisKey.DEVICE_PROPERTY, deviceCode);
    }
}
