package cn.sinozg.applet.biz.device.service;

import cn.sinozg.applet.biz.device.entity.DeviceInfo;
import cn.sinozg.applet.biz.device.vo.request.DeviceInfoCreateRequest;
import cn.sinozg.applet.biz.device.vo.request.DeviceInfoPageRequest;
import cn.sinozg.applet.biz.device.vo.request.DeviceInfoProtocolRequest;
import cn.sinozg.applet.biz.device.vo.request.DeviceInfoUpdateRequest;
import cn.sinozg.applet.biz.device.vo.response.DeviceInfoInfoResponse;
import cn.sinozg.applet.biz.device.vo.response.DeviceInfoPageResponse;
import cn.sinozg.applet.biz.device.vo.response.device.FunctionElement;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.PagingRequest;
import cn.sinozg.applet.iot.protocol.vo.response.DeviceInfoProtocolResponse;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
* 设备信息表 服务类
* @Description
* @Copyright Copyright (c) 2023
* @author wang.pf
* @since 2023-12-03 16:27:01
*/
public interface DeviceInfoService extends IService<DeviceInfo> {

    /**
    * 新增设备信息表
    * @param params 设备信息表
    */
    void createInfo (DeviceInfoCreateRequest params);

    /**
    * 查询详情
    * @param id 主键
    * @return 对象
    */
    DeviceInfoInfoResponse getInfoById(String id);

    /**
    * 修改设备信息表
    * @param params 设备信息表
    */
    void updateInfo(DeviceInfoUpdateRequest params);

    /**
    * 分页查询
    * @param page 分页对象
    * @param params 参数
    * @return 分页集合
    */
    BasePageResponse<List<DeviceInfoPageResponse>> pageInfo(PagingRequest page, DeviceInfoPageRequest params);


    /**
     * 设备消息用于协议
     * @param params 查询参数
     * @return 数据
     */
    DeviceInfoProtocolResponse deviceInfoForProtocol(DeviceInfoProtocolRequest params);

    String protocolIdForDevice(DeviceInfoProtocolRequest params);

    /**
     * 保存设备相关的信息
     * @param params 参数
     */
    void saveDeviceInfoByProtocol(DeviceInfoProtocolResponse params);

    /**
     * 获取设备的功能信息
     * @param prodKey 产品key
     * @param deviceCode 设备code
     * @param funId 功能id
     * @return 功能信息
     */
    ImmutablePair<DeviceInfo, FunctionElement> functionInfo (String prodKey, String deviceCode, String funId);
}
