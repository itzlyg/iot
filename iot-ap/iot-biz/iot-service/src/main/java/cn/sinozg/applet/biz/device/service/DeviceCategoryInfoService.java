package cn.sinozg.applet.biz.device.service;

import cn.sinozg.applet.biz.device.entity.DeviceCategoryInfo;
import cn.sinozg.applet.biz.device.vo.request.DeviceCategoryInfoCreateRequest;
import cn.sinozg.applet.biz.device.vo.request.DeviceCategoryInfoPageRequest;
import cn.sinozg.applet.biz.device.vo.request.DeviceCategoryInfoUpdateRequest;
import cn.sinozg.applet.biz.device.vo.response.DeviceCategoryInfoInfoResponse;
import cn.sinozg.applet.biz.device.vo.response.DeviceCategoryInfoPageResponse;
import cn.sinozg.applet.biz.protocol.vo.request.SelectByNameRequest;
import cn.sinozg.applet.biz.system.vo.response.DictListResponse;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.PagingRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
* 设备分类信息表 服务类
* @Description
* @Copyright Copyright (c) 2023
* @author wang.pf
* @since 2023-12-03 16:48:09
*/
public interface DeviceCategoryInfoService extends IService<DeviceCategoryInfo> {

    /**
    * 新增设备分类信息表
    * @param params 设备分类信息表
    */
    void createInfo (DeviceCategoryInfoCreateRequest params);

    /**
    * 查询详情
    * @param id 主键
    * @return 对象
    */
    DeviceCategoryInfoInfoResponse getInfoById(String id);

    /**
    * 修改设备分类信息表
    * @param params 设备分类信息表
    */
    void updateInfo(DeviceCategoryInfoUpdateRequest params);

    /**
    * 分页查询
    * @param page 分页对象
    * @param params 参数
    * @return 分页集合
    */
    BasePageResponse<List<DeviceCategoryInfoPageResponse>> pageInfo(PagingRequest page, DeviceCategoryInfoPageRequest params);

    List<DictListResponse> moduleList(SelectByNameRequest name);

    List<DeviceCategoryInfoInfoResponse> findSubCategory(String id);
}
