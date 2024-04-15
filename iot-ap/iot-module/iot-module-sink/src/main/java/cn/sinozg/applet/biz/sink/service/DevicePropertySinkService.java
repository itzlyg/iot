package cn.sinozg.applet.biz.sink.service;


import cn.sinozg.applet.biz.sink.vo.request.PropertyInfoAddRequest;
import cn.sinozg.applet.biz.sink.vo.request.DevicePropertyHistoryRequest;
import cn.sinozg.applet.biz.sink.vo.response.PropertyInfoHistoryResponse;

import java.util.List;
import java.util.Map;

/**
 * 设备属性落地接口
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-12-05 20:26:06
 */
public interface DevicePropertySinkService {

    /**
     * 按时间范围取设备指定属性的历史数据
     *
     * @param params 参数
     * @return 集合
     */
    List<PropertyInfoHistoryResponse> devicePropertyHistory(DevicePropertyHistoryRequest params);

    /**
     * 添加多个属性
     *
     * @param params   属性上报存储对象
     */
    void addProperties(PropertyInfoAddRequest params);

    /**
     * 获取最新的一条数据
     * @param deviceCode 设备id
     * @return 记录
     */
    Map<String, Object> lastRecord (String deviceCode);
}
