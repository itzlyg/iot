package cn.sinozg.applet.biz.sink.service;

import cn.sinozg.applet.biz.sink.entity.PropertyLog;
import cn.sinozg.applet.biz.sink.vo.request.DevicePropertyHistoryRequest;
import cn.sinozg.applet.biz.sink.vo.response.PropertyInfoHistoryResponse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* 产品属性日志记录表 服务类
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-03-07 15:35:53
*/
public interface PropertyLogService extends IService<PropertyLog> {
    List<PropertyInfoHistoryResponse> devicePropertyHistory (DevicePropertyHistoryRequest params);

    PropertyLog lastRecord(String deviceCode);
}
