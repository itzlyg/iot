package cn.sinozg.applet.biz.device.service;

import cn.sinozg.applet.biz.device.entity.DeviceGroupInfo;
import cn.sinozg.applet.biz.device.vo.request.DeviceGroupInfoCreateRequest;
import cn.sinozg.applet.biz.device.vo.request.DeviceGroupInfoPageRequest;
import cn.sinozg.applet.biz.device.vo.request.DeviceGroupInfoUpdateRequest;
import cn.sinozg.applet.biz.device.vo.response.DeviceGroupInfoInfoResponse;
import cn.sinozg.applet.biz.device.vo.response.DeviceGroupInfoPageResponse;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.PagingRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
* 设备分组信息表 服务类
* @Description
* @Copyright Copyright (c) 2023
* @author wang.pf
* @since 2023-12-03 16:27:01
*/
public interface DeviceGroupInfoService extends IService<DeviceGroupInfo> {

    /**
    * 新增设备分组信息表
    * @param params 设备分组信息表
    */
    void createInfo (DeviceGroupInfoCreateRequest params);

    /**
    * 查询详情
    * @param id 主键
    * @return 对象
    */
    DeviceGroupInfoInfoResponse getInfoById(String id);

    void delete (String id);

    /**
    * 修改设备分组信息表
    * @param params 设备分组信息表
    */
    void updateInfo(DeviceGroupInfoUpdateRequest params);

    /**
    * 分页查询
    * @param page 分页对象
    * @param params 参数
    * @return 分页集合
    */
    BasePageResponse<List<DeviceGroupInfoPageResponse>> pageInfo(PagingRequest page, DeviceGroupInfoPageRequest params);
}
