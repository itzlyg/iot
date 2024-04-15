package cn.sinozg.applet.biz.device.mapper;

import cn.sinozg.applet.biz.device.entity.DeviceGroupInfo;
import cn.sinozg.applet.biz.device.vo.request.DeviceGroupInfoPageRequest;
import cn.sinozg.applet.biz.device.vo.response.DeviceGroupInfoPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
* 设备分组信息表 Mapper 接口
* @Description
* @Copyright Copyright (c) 2023
* @author wang.pf
* @since 2023-12-03 16:27:01
*/
public interface DeviceGroupInfoMapper extends BaseMapper<DeviceGroupInfo> {

    /**
    * 根据条件分页查询设备分组信息表列表
    *
    * @param page 分页信息
    * @param params 设备分组信息表信息
    * @return 设备分组信息表信息集合信息
    */
    IPage<DeviceGroupInfoPageResponse> pageInfo(Page<DeviceGroupInfoPageResponse> page, @Param("p") DeviceGroupInfoPageRequest params);
}
