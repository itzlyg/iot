package cn.sinozg.applet.biz.sink.service.impl;

import cn.sinozg.applet.biz.sink.entity.PropertyLog;
import cn.sinozg.applet.biz.sink.mapper.PropertyLogMapper;
import cn.sinozg.applet.biz.sink.service.PropertyLogService;
import cn.sinozg.applet.biz.sink.vo.request.DevicePropertyHistoryRequest;
import cn.sinozg.applet.biz.sink.vo.response.PropertyInfoHistoryResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* 产品属性日志记录表 服务实现类
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-03-07 15:35:53
*/
@Service
public class PropertyLogServiceImpl extends ServiceImpl<PropertyLogMapper, PropertyLog> implements PropertyLogService {

    @Resource
    private PropertyLogMapper mapper;


    @Override
    public List<PropertyInfoHistoryResponse> devicePropertyHistory(DevicePropertyHistoryRequest params) {
        return mapper.devicePropertyHistory(params);
    }

    @Override
    public PropertyLog lastRecord(String deviceCode) {
        return this.getOne(new LambdaQueryWrapper<PropertyLog>()
                .eq(PropertyLog::getDeviceCode, deviceCode).orderByDesc(PropertyLog::getTs)
                .last("limit 1"));
    }
}
