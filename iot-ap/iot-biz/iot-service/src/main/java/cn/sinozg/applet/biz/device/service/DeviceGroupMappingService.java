package cn.sinozg.applet.biz.device.service;

import cn.sinozg.applet.biz.device.entity.DeviceGroupMapping;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* 设备分组关联 服务类
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-12-15 15:19:25
*/
public interface DeviceGroupMappingService extends IService<DeviceGroupMapping> {

    /**
     * 新增设备分组关联
     * @param groupId 分组id
     * @param deviceList 设备集合
     */
    void saveMappingInfo (String groupId, List<String> deviceList);

    List<String> deviceList(String groupId);
}
