package cn.sinozg.applet.biz.device.service.impl;

import cn.sinozg.applet.biz.device.entity.DeviceGroupMapping;
import cn.sinozg.applet.biz.device.mapper.DeviceGroupMappingMapper;
import cn.sinozg.applet.biz.device.service.DeviceGroupMappingService;
import cn.sinozg.applet.common.utils.PojoUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* 设备分组关联 服务实现类
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-12-15 15:19:25
*/
@Service
public class DeviceGroupMappingServiceImpl extends ServiceImpl<DeviceGroupMappingMapper, DeviceGroupMapping> implements DeviceGroupMappingService {

    @Resource
    private DeviceGroupMappingMapper mapper;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void saveMappingInfo(String groupId, List<String> deviceList) {
        this.remove(new LambdaQueryWrapper<DeviceGroupMapping>().eq(DeviceGroupMapping::getGroupId, groupId));
        if (CollectionUtils.isNotEmpty(deviceList)) {
            List<DeviceGroupMapping> list = new ArrayList<>();
            deviceList.forEach(d -> {
                DeviceGroupMapping mapping = new DeviceGroupMapping();
                mapping.setGroupId(groupId);
                mapping.setDeviceId(d);
                list.add(mapping);
            });
            this.saveBatch(list);
        }
    }

    @Override
    public List<String> deviceList(String groupId){
        List<DeviceGroupMapping> list = this.list(new LambdaQueryWrapper<DeviceGroupMapping>().eq(DeviceGroupMapping::getGroupId, groupId));
        return PojoUtil.toList(list, DeviceGroupMapping::getDeviceId);
    }
}
