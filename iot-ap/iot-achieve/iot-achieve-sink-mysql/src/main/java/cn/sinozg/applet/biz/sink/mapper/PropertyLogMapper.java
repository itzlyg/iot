package cn.sinozg.applet.biz.sink.mapper;

import cn.sinozg.applet.biz.sink.entity.PropertyLog;
import cn.sinozg.applet.biz.sink.vo.request.DevicePropertyHistoryRequest;
import cn.sinozg.applet.biz.sink.vo.response.PropertyInfoHistoryResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 产品属性日志记录表 Mapper 接口
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-03-07 15:35:53
*/
public interface PropertyLogMapper extends BaseMapper<PropertyLog> {
    List<PropertyInfoHistoryResponse> devicePropertyHistory (@Param("p") DevicePropertyHistoryRequest params);
}
