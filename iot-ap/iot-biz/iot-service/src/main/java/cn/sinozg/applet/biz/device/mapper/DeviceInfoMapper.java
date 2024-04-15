package cn.sinozg.applet.biz.device.mapper;

import cn.sinozg.applet.biz.device.entity.DeviceInfo;
import cn.sinozg.applet.biz.device.vo.request.DeviceInfoPageRequest;
import cn.sinozg.applet.biz.device.vo.request.DeviceInfoProtocolRequest;
import cn.sinozg.applet.biz.device.vo.response.DeviceInfoPageResponse;
import cn.sinozg.applet.iot.protocol.vo.response.DeviceInfoProtocolResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
* 设备信息表 Mapper 接口
* @Description
* @Copyright Copyright (c) 2023
* @author wang.pf
* @since 2023-12-03 16:27:01
*/
public interface DeviceInfoMapper extends BaseMapper<DeviceInfo> {

    /**
    * 根据条件分页查询设备信息表列表
    *
    * @param page 分页信息
    * @param params 设备信息表信息
    * @return 设备信息表信息集合信息
    */
    IPage<DeviceInfoPageResponse> pageInfo(Page<DeviceInfoPageResponse> page, @Param("p") DeviceInfoPageRequest params);

    DeviceInfoProtocolResponse deviceInfoForProtocol(@Param("p") DeviceInfoProtocolRequest params);

    String protocolIdForDevice(@Param("p") DeviceInfoProtocolRequest params);
}
