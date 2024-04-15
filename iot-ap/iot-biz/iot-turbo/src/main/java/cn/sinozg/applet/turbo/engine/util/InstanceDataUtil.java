package cn.sinozg.applet.turbo.engine.util;

import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.turbo.engine.common.DataType;
import cn.sinozg.applet.turbo.engine.model.InstanceDataVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstanceDataUtil {

    private InstanceDataUtil() {}

    public static Map<String, InstanceDataVo> getInstanceDataMap(List<InstanceDataVo> instanceDataList) {
        if (CollectionUtils.isEmpty(instanceDataList)) {
            return new HashMap<>(16);
        }
        Map<String, InstanceDataVo> instanceDataMap = new HashMap<>(16);
        instanceDataList.forEach(instanceData -> instanceDataMap.put(instanceData.getKey(), instanceData));
        return instanceDataMap;
    }

    public static Map<String, InstanceDataVo> getInstanceDataMap(String instanceDataStr) {
        if (StringUtils.isBlank(instanceDataStr)) {
            return new HashMap<>(16);
        }
        List<InstanceDataVo> instanceDataList = JsonUtil.toPojos(instanceDataStr, InstanceDataVo.class);
        return getInstanceDataMap(instanceDataList);
    }

    public static List<InstanceDataVo> getInstanceDataList(Map<String, InstanceDataVo> instanceDataMap) {
        if (MapUtils.isEmpty(instanceDataMap)) {
            return new ArrayList<>();
        }
        List<InstanceDataVo> instanceDataList = new ArrayList<>();
        instanceDataMap.forEach((key, instanceData) -> instanceDataList.add(instanceData));
        return instanceDataList;
    }

    public static String getInstanceDataListStr(Map<String, InstanceDataVo> instanceDataMap) {
        if (MapUtils.isEmpty(instanceDataMap)) {
            return JsonUtil.toJson(CollectionUtils.EMPTY_COLLECTION);
        }
        return JsonUtil.toJson(instanceDataMap.values());
    }

    public static Map<String, Object> parseInstanceDataMap(Map<String, InstanceDataVo> instanceDataMap) {
        if (MapUtils.isEmpty(instanceDataMap)) {
            return new HashMap<>(16);
        }
        Map<String, Object> dataMap = new HashMap<>(16);
        instanceDataMap.forEach((keyName, instanceData) -> dataMap.put(keyName, parseInstanceData(instanceData)));
        return dataMap;
    }

    private static Object parseInstanceData(InstanceDataVo instanceData) {
        if (instanceData == null) {
            return null;
        }
        String dataTypeStr = instanceData.getType();
        DataType dataType = DataType.getType(dataTypeStr);

        // TODO: 2019/12/16
        return instanceData.getValue();
    }
}
