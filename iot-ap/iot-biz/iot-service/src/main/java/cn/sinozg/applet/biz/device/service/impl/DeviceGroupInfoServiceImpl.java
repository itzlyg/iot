package cn.sinozg.applet.biz.device.service.impl;

import cn.sinozg.applet.biz.device.entity.DeviceGroupInfo;
import cn.sinozg.applet.biz.device.mapper.DeviceGroupInfoMapper;
import cn.sinozg.applet.biz.device.service.DeviceGroupInfoService;
import cn.sinozg.applet.biz.device.service.DeviceGroupMappingService;
import cn.sinozg.applet.biz.device.vo.request.DeviceGroupInfoCreateRequest;
import cn.sinozg.applet.biz.device.vo.request.DeviceGroupInfoPageRequest;
import cn.sinozg.applet.biz.device.vo.request.DeviceGroupInfoUpdateRequest;
import cn.sinozg.applet.biz.device.vo.response.DeviceGroupInfoInfoResponse;
import cn.sinozg.applet.biz.device.vo.response.DeviceGroupInfoPageResponse;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.PagingRequest;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.BizUtil;
import cn.sinozg.applet.common.utils.PageUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
* 设备分组信息表 服务实现类
* @Description
* @Copyright Copyright (c) 2023
* @author wang.pf
* @since 2023-12-03 16:27:01
*/
@Service
public class DeviceGroupInfoServiceImpl extends ServiceImpl<DeviceGroupInfoMapper, DeviceGroupInfo> implements DeviceGroupInfoService {

    @Resource
    private DeviceGroupInfoMapper mapper;

    @Resource
    private DeviceGroupMappingService groupMappingService;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void createInfo(DeviceGroupInfoCreateRequest params) {
        DeviceGroupInfo entity = PojoUtil.copyBean(params, DeviceGroupInfo.class);
        this.save(entity);
        groupMappingService.saveMappingInfo(entity.getId(), params.getGroupDeviceJson());
    }

    @Override
    public DeviceGroupInfoInfoResponse getInfoById(String id) {
        DeviceGroupInfo entity = this.infoById(id);
        DeviceGroupInfoInfoResponse response = PojoUtil.copyBean(entity, DeviceGroupInfoInfoResponse.class);
        response.setGroupDeviceJson(groupMappingService.deviceList(entity.getId()));
        return response;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void updateInfo(DeviceGroupInfoUpdateRequest params) {
        infoById(params.getId());
        DeviceGroupInfo entity = PojoUtil.copyBean(params, DeviceGroupInfo.class);
        this.updateById(entity);
        groupMappingService.saveMappingInfo(entity.getId(), params.getGroupDeviceJson());
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete (String id){
        infoById(id);
        this.removeById(id);
        groupMappingService.saveMappingInfo(id, null);
    }

    @Override
    public BasePageResponse<List<DeviceGroupInfoPageResponse>> pageInfo(PagingRequest page, DeviceGroupInfoPageRequest params) {
        PageUtil<DeviceGroupInfoPageResponse, DeviceGroupInfoPageRequest> pu = (p, q) -> mapper.pageInfo(p, q);
        params.setUid(BizUtil.demoUser());
        return pu.page(page, params);
    }

    /**
     * 查询详情
     * @param id 主键id
     * @return 实体对象
     */
    private DeviceGroupInfo infoById(String id){
        DeviceGroupInfo entity = this.getById(id);
        if (entity == null) {
            throw new CavException("未找到设备分组信息表！");
        }
        return entity;
    }
}
