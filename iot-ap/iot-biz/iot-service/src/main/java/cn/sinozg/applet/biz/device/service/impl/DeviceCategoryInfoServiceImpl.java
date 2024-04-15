package cn.sinozg.applet.biz.device.service.impl;

import cn.sinozg.applet.biz.device.entity.DeviceCategoryInfo;
import cn.sinozg.applet.biz.device.mapper.DeviceCategoryInfoMapper;
import cn.sinozg.applet.biz.device.service.DeviceCategoryInfoService;
import cn.sinozg.applet.biz.device.vo.request.DeviceCategoryInfoCreateRequest;
import cn.sinozg.applet.biz.device.vo.request.DeviceCategoryInfoPageRequest;
import cn.sinozg.applet.biz.device.vo.request.DeviceCategoryInfoUpdateRequest;
import cn.sinozg.applet.biz.device.vo.response.DeviceCategoryInfoInfoResponse;
import cn.sinozg.applet.biz.device.vo.response.DeviceCategoryInfoPageResponse;
import cn.sinozg.applet.biz.protocol.vo.request.SelectByNameRequest;
import cn.sinozg.applet.biz.system.vo.response.DictListResponse;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.PagingRequest;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.BizUtil;
import cn.sinozg.applet.common.utils.PageUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* 设备分类信息表 服务实现类
* @Description
* @Copyright Copyright (c) 2023
* @author wang.pf
* @since 2023-12-03 16:48:09
*/
@Service
public class DeviceCategoryInfoServiceImpl extends ServiceImpl<DeviceCategoryInfoMapper, DeviceCategoryInfo> implements DeviceCategoryInfoService {

    @Resource
    private DeviceCategoryInfoMapper mapper;

    @Override
    public void createInfo(DeviceCategoryInfoCreateRequest params) {
        DeviceCategoryInfo entity = PojoUtil.copyBean(params, DeviceCategoryInfo.class);
        this.save(entity);
    }

    @Override
    public DeviceCategoryInfoInfoResponse getInfoById(String id) {
        DeviceCategoryInfo entity = this.infoById(id);
        return PojoUtil.copyBean(entity, DeviceCategoryInfoInfoResponse.class);
    }

    @Override
    public void updateInfo(DeviceCategoryInfoUpdateRequest params) {
        DeviceCategoryInfo entity = PojoUtil.copyBean(params, DeviceCategoryInfo.class);
        this.updateById(entity);
    }

    @Override
    public BasePageResponse<List<DeviceCategoryInfoPageResponse>> pageInfo(PagingRequest page, DeviceCategoryInfoPageRequest params) {
        PageUtil<DeviceCategoryInfoPageResponse, DeviceCategoryInfoPageRequest> pu = (p, q) -> mapper.pageInfo(p, q);
        params.setUid(BizUtil.demoUser());
        return pu.page(page, params);
    }
    @Override
    public List<DictListResponse> moduleList(SelectByNameRequest params) {
        params.setUid(BizUtil.demoUser());
        return mapper.moduleList(params);
    }
    @Override
    public List<DeviceCategoryInfoInfoResponse> findSubCategory(String id) {
        DeviceCategoryInfoPageRequest params = new DeviceCategoryInfoPageRequest();
        params.setParentCode(id);
        return mapper.findSubCategory(params);
    }
    /**
     * 查询详情
     * @param id 主键id
     * @return 实体对象
     */
    private DeviceCategoryInfo infoById(String id){
        DeviceCategoryInfo entity = this.getById(id);
        if (entity == null) {
            throw new CavException("未找到设备分类信息表！");
        }
        return entity;
    }
}
