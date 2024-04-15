package cn.sinozg.applet.biz.device.service.impl;

import cn.sinozg.applet.biz.device.entity.DeviceInfo;
import cn.sinozg.applet.biz.device.mapper.DeviceInfoMapper;
import cn.sinozg.applet.biz.device.service.DeviceInfoService;
import cn.sinozg.applet.biz.device.vo.request.DeviceInfoCreateRequest;
import cn.sinozg.applet.biz.device.vo.request.DeviceInfoPageRequest;
import cn.sinozg.applet.biz.device.vo.request.DeviceInfoProtocolRequest;
import cn.sinozg.applet.biz.device.vo.request.DeviceInfoUpdateRequest;
import cn.sinozg.applet.biz.device.vo.response.DeviceInfoInfoResponse;
import cn.sinozg.applet.biz.device.vo.response.DeviceInfoPageResponse;
import cn.sinozg.applet.biz.device.vo.response.device.AttributeElementInfo;
import cn.sinozg.applet.biz.device.vo.response.device.FunctionElement;
import cn.sinozg.applet.biz.device.vo.response.device.FunctionParamElement;
import cn.sinozg.applet.biz.product.entity.ProdInfo;
import cn.sinozg.applet.biz.product.service.ProdInfoService;
import cn.sinozg.applet.biz.product.vo.request.table.FunctionTable;
import cn.sinozg.applet.biz.sink.service.DevicePropertySinkService;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.PagingRequest;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.BizUtil;
import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.common.utils.PageUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.iot.protocol.vo.response.DeviceInfoProtocolResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
* 设备信息表 服务实现类
* @Description
* @Copyright Copyright (c) 2023
* @author wang.pf
* @since 2023-12-03 16:27:01
*/
@Service
public class DeviceInfoServiceImpl extends ServiceImpl<DeviceInfoMapper, DeviceInfo> implements DeviceInfoService {

    @Resource
    private DeviceInfoMapper mapper;
    @Resource
    private ProdInfoService prodInfoService;

    @Resource
    private DevicePropertySinkService propertySinkService;

    @Override
    public void createInfo(DeviceInfoCreateRequest params) {
        DeviceInfo entity = PojoUtil.copyBean(params, DeviceInfo.class);
        this.save(entity);
    }

    @Override
    public DeviceInfoInfoResponse getInfoById(String id) {
        DeviceInfo entity = this.infoById(id);
        DeviceInfoInfoResponse response = PojoUtil.copyBean(entity, DeviceInfoInfoResponse.class);
        ProdInfo prodInfo = prodInfoService.infoByIdOrKey(null, entity.getProdKey());
        List<AttributeElementInfo> attributeList = JsonUtil.toPojos(prodInfo.getAttributeJson(), AttributeElementInfo.class);
        if (CollectionUtils.isNotEmpty(attributeList)) {
            Map<String, Object> propertyMap = propertySinkService.lastRecord(entity.getDeviceCode());
            attributeList.forEach(a -> a.setValue(MapUtils.getObject(propertyMap, a.getIdentifier())));
            response.setAttributeInfoList(attributeList);
        }
        List<FunctionTable> list = JsonUtil.toPojos(prodInfo.getFunctionJson(), FunctionTable.class);
        List<FunctionElement> elementList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(item -> {
                FunctionElement element = PojoUtil.copyBean(item, FunctionElement.class);
                element.setParamList(PojoUtil.copyList(item.getInputParameters(), FunctionParamElement.class));
                elementList.add(element);
            });
        }
        response.setFunctionInfoList(elementList);
        return response;
    }

    @Override
    public void updateInfo(DeviceInfoUpdateRequest params) {
        DeviceInfo entity = PojoUtil.copyBean(params, DeviceInfo.class);
        this.updateById(entity);
    }

    @Override
    public BasePageResponse<List<DeviceInfoPageResponse>> pageInfo(PagingRequest page, DeviceInfoPageRequest params) {
        PageUtil<DeviceInfoPageResponse, DeviceInfoPageRequest> pu = (p, q) -> mapper.pageInfo(p, q);
        params.setUid(BizUtil.demoUser());
        return pu.page(page, params);
    }

    @Override
    public DeviceInfoProtocolResponse deviceInfoForProtocol(DeviceInfoProtocolRequest params) {
        DeviceInfoProtocolResponse response = mapper.deviceInfoForProtocol(params);
        if (params.isNotNull() && response == null) {
            throw new CavException("未找到协议相关的设备信息！");
        }
        return response;
    }

    @Override
    public String protocolIdForDevice(DeviceInfoProtocolRequest params) {
        return mapper.protocolIdForDevice(params);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void saveDeviceInfoByProtocol(DeviceInfoProtocolResponse params) {
        String id = params.getId();
        boolean insert = StringUtils.isBlank(id);
        // 新增
        if (insert) {
            DeviceInfo device = new DeviceInfo();
            device.setDeviceCode(params.getDeviceCode());
            device.setDeviceName(params.getDeviceName());
            device.setProdKey(params.getProdKey());
            device.setDeviceType(params.getModel());
            device.setCreatedBy(params.getUid());
            // 父设备处理 todo
            this.save(device);
            id = params.getId();
        } else {
            DeviceInfo update = new DeviceInfo();
            update.setId(params.getId());
            // 更换网关或者重新注册更新父设备id
            if (StringUtils.isNotBlank(params.getParentId())) {
               // todo
            }
            update.setDeviceType(params.getModel());
            this.updateById(update);
        }
        // 更新设备 状态 上线 下线 todo
        if (params.getOnline() != null) {

        }
        // 更新 位置信息
        if (params.getLocate() != null) {

        }
    }

    /**
     * 获取设备的功能信息
     * @param prodKey 产品key
     * @param deviceCode 设备code
     * @param funId 功能id
     * @return 功能信息
     */
    @Override
    public Pair<DeviceInfo, FunctionElement> functionInfo (String prodKey, String deviceCode, String funId){
        List<DeviceInfo> list = this.list(new LambdaQueryWrapper<DeviceInfo>().eq(DeviceInfo::getDeviceCode, deviceCode)
                .eq(DeviceInfo::getProdKey, prodKey));
        if (PojoUtil.single(list)) {
            ProdInfo prodInfo = prodInfoService.infoByIdOrKey(null, prodKey);
            List<FunctionTable> functions = JsonUtil.toPojos(prodInfo.getFunctionJson(), FunctionTable.class);
            if (CollectionUtils.isNotEmpty(functions)) {
                for (FunctionTable fun : functions) {
                    if (funId.equals(fun.getIdentifier())) {
                        return Pair.of(list.get(0), PojoUtil.copyBean(fun, FunctionElement.class));
                    }
                }
            }
        }
        return null;
    }

    /**
     * 查询详情
     * @param id 主键id
     * @return 实体对象
     */
    private DeviceInfo infoById(String id){
        DeviceInfo entity = this.getById(id);
        if (entity == null) {
            throw new CavException("未找到设备信息表！");
        }
        return entity;
    }
}
