package cn.sinozg.applet.biz.device.mapper;

import cn.sinozg.applet.biz.device.entity.DeviceCategoryInfo;
import cn.sinozg.applet.biz.device.vo.request.DeviceCategoryInfoPageRequest;
import cn.sinozg.applet.biz.device.vo.response.DeviceCategoryInfoInfoResponse;
import cn.sinozg.applet.biz.device.vo.response.DeviceCategoryInfoPageResponse;
import cn.sinozg.applet.biz.protocol.vo.request.SelectByNameRequest;
import cn.sinozg.applet.biz.system.vo.response.DictListResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 设备分类信息表 Mapper 接口
* @Description
* @Copyright Copyright (c) 2023
* @author wang.pf
* @since 2023-12-03 16:48:09
*/
public interface DeviceCategoryInfoMapper extends BaseMapper<DeviceCategoryInfo> {

    /**
    * 根据条件分页查询设备分类信息表列表
    *
    * @param page 分页信息
    * @param params 设备分类信息表信息
    * @return 设备分类信息表信息集合信息
    */
    IPage<DeviceCategoryInfoPageResponse> pageInfo(Page<DeviceCategoryInfoPageResponse> page, @Param("p") DeviceCategoryInfoPageRequest params);

    List<DictListResponse> moduleList (@Param("p") SelectByNameRequest categoryName);

    List<DeviceCategoryInfoInfoResponse> findSubCategory (@Param("p") DeviceCategoryInfoPageRequest params);
}
