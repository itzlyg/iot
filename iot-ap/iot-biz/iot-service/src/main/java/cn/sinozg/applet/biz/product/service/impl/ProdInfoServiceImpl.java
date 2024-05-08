package cn.sinozg.applet.biz.product.service.impl;

import cn.sinozg.applet.biz.com.model.TmBaseDataTypeInfo;
import cn.sinozg.applet.biz.com.model.TmDataTypeInfo;
import cn.sinozg.applet.biz.com.model.TmServiceOutInfo;
import cn.sinozg.applet.biz.com.vo.request.TmSinkRequest;
import cn.sinozg.applet.biz.com.vo.response.TmProtocolResponse;
import cn.sinozg.applet.biz.product.entity.ProdInfo;
import cn.sinozg.applet.biz.product.mapper.ProdInfoMapper;
import cn.sinozg.applet.biz.product.service.ProdInfoService;
import cn.sinozg.applet.biz.product.vo.request.ProdInfoCreateRequest;
import cn.sinozg.applet.biz.product.vo.request.ProdInfoPageRequest;
import cn.sinozg.applet.biz.product.vo.request.ProdInfoUpdateRequest;
import cn.sinozg.applet.biz.product.vo.request.table.AttributeTable;
import cn.sinozg.applet.biz.product.vo.request.table.EventTable;
import cn.sinozg.applet.biz.product.vo.request.table.FunctionTable;
import cn.sinozg.applet.biz.product.vo.request.table.ProdInfoJsonList;
import cn.sinozg.applet.biz.product.vo.request.table.conf.FunctionTableConfig;
import cn.sinozg.applet.biz.product.vo.response.ProdInfoInfoResponse;
import cn.sinozg.applet.biz.product.vo.response.ProdInfoPageResponse;
import cn.sinozg.applet.biz.product.vo.response.ProdListResponse;
import cn.sinozg.applet.biz.sink.common.enums.DbDataType;
import cn.sinozg.applet.biz.sink.service.DbStructureService;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.PagingRequest;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.BizUtil;
import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.common.utils.PageUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品消息表 服务实现类
 *
 * @author zy
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-27 21:44:29
 */
@Service
public class ProdInfoServiceImpl extends ServiceImpl<ProdInfoMapper, ProdInfo> implements ProdInfoService {

    @Resource
    private ProdInfoMapper mapper;

    @Resource
    private DbStructureService dbStructureService;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void createInfo(ProdInfoCreateRequest params) {
        ProdInfo entity = prodJsonInfo(null, params);
        entity.setProdKey(BizUtil.randomCode(8, this, ProdInfo::getProdKey));
        this.save(entity);
        syncThingModel(true, entity.getProdKey(), params);
    }

    @Override
    public ProdInfoInfoResponse getInfoById(String id) {
        ProdInfo entity = this.infoByIdOrKey(id, null);
        ProdInfoInfoResponse response = PojoUtil.copyBean(entity, ProdInfoInfoResponse.class);
        prodJsonInfo(entity, response);
        return response;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void updateInfo(ProdInfoUpdateRequest params) {
        ProdInfo dbInfo = this.infoByIdOrKey(params.getId(), null);
        ProdInfo entity = prodJsonInfo(null, params);
        this.updateById(entity);
        syncThingModel(false, dbInfo.getProdKey(), params);
    }

    @Override
    public List<ProdListResponse> selectList(String name){
        name = BizUtil.demoUser();
        return mapper.selectProdList(name);
    }

    @Override
    public BasePageResponse<List<ProdInfoPageResponse>> pageInfo(PagingRequest page, ProdInfoPageRequest params) {
        PageUtil<ProdInfoPageResponse, ProdInfoPageRequest> pu = (p, q) -> mapper.pageInfo(p, q);
        params.setUid(BizUtil.demoUser());
        return pu.page(page, params);
    }

    /**
     * 查询详情
     *
     * @param id 主键id
     * @return 实体对象
     */
    @Override
    public ProdInfo infoByIdOrKey(String id, String prodKey) {
        ProdInfo prodInfo = this.getOne(new LambdaQueryWrapper<ProdInfo>()
                .eq(StringUtils.isNotBlank(id), ProdInfo::getId, id)
                .eq(StringUtils.isNotBlank(prodKey), ProdInfo::getProdKey, prodKey));
        if (prodInfo == null) {
            throw new CavException("未找到产品信息！");
        }
        return prodInfo;
    }

    /**
     * 获取协议需要的物模型数据
     *
     * @param prodKey 产品key
     * @return 物模型数据
     */
    @Override
    public TmProtocolResponse tmInfo(String prodKey) {
        ProdInfo info = this.infoByIdOrKey(null, prodKey);
        TmProtocolResponse response = new TmProtocolResponse();
        String attributeJson = info.getAttributeJson();
        List<AttributeTable> attributes = JsonUtil.toPojos(attributeJson, AttributeTable.class);
        response.setId(info.getId());
        response.setAttributes(toAttributes(attributes));
        String funJson = info.getFunctionJson();
        List<FunctionTable> funList = JsonUtil.toPojos(funJson, FunctionTable.class);
        response.setService(toFunctions(funList));
        response.setProdKey(info.getProdKey());
        return response;
    }

    /**
     * json 转化
     * @param entity 对象
     * @param request 参数
     */
    private ProdInfo prodJsonInfo(ProdInfo entity, ProdInfoJsonList request){
        if (entity == null) {
            entity = PojoUtil.copyBean(request, ProdInfo.class);
            entity.setAttributeJson(toJson(request.getAttributeJson()));
            entity.setFunctionJson(toJson(request.getFunctionJson()));
            entity.setEventJson(toJson(request.getEventJson()));
        } else {
            request.setAttributeJson(JsonUtil.toPojos(entity.getAttributeJson(), AttributeTable.class));
            request.setFunctionJson(JsonUtil.toPojos(entity.getFunctionJson(), FunctionTable.class));
            request.setEventJson(JsonUtil.toPojos(entity.getEventJson(), EventTable.class));
        }
        return entity;
    }

    private String toJson (List<?> list){
        if (CollectionUtils.isEmpty(list)) {
            list = new ArrayList<>();
        }
        return JsonUtil.toJson(list);
    }

    /**
     * 同步数据结构
     * @param create 是否新建
     * @param prodKey 产品key
     * @param request json
     */
    private void syncThingModel(boolean create, String prodKey, ProdInfoJsonList request){
        TmSinkRequest sinkRequest = new TmSinkRequest();
        sinkRequest.setProdKey(prodKey);
        sinkRequest.setAttributes(toAttributes(request.getAttributeJson()));
        dbStructureService.syncThingModel(sinkRequest, create);
    }


    /**
     * 转成协议和 时序数据库所需的结构
     *
     * @param attributes 结构
     * @return 数据
     */
    private List<TmBaseDataTypeInfo> toAttributes(List<AttributeTable> attributes) {
        List<TmBaseDataTypeInfo> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(attributes)) {
            for (AttributeTable at : attributes) {
                TmBaseDataTypeInfo ta = new TmBaseDataTypeInfo();
                ta.setIdentifier(at.getIdentifier());
                ta.setDataType(dataTypeInfo(at.getDataType()));
                list.add(ta);
            }
        }
        return list;
    }

    /**
     * 转为指令用的 服务数据
     * @param funList 配置
     * @return 服务
     */
    private List<TmServiceOutInfo> toFunctions (List<FunctionTable> funList){
        List<TmServiceOutInfo> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(funList)) {
            for (FunctionTable f : funList) {
                TmServiceOutInfo out = new TmServiceOutInfo();
                out.setIdentifier(f.getIdentifier());
                List<TmBaseDataTypeInfo> inputData = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(f.getInputParameters())) {
                    for (FunctionTableConfig inputParameter : f.getInputParameters()) {
                        TmBaseDataTypeInfo dataTypeInfo = new TmBaseDataTypeInfo();
                        dataTypeInfo.setIdentifier(inputParameter.getParameterIdentifier());
                        dataTypeInfo.setDataType(dataTypeInfo(inputParameter.getDataType()));
                        inputData.add(dataTypeInfo);
                    }
                }
                out.setInputData(inputData);
                list.add(out);
            }
        }
        return list;
    }

    /**
     * 数据属性
     * @param dataType 数据类型
     * @return 数据类型
     */
    private TmDataTypeInfo dataTypeInfo (String dataType){
        TmDataTypeInfo dt = new TmDataTypeInfo();
        dt.setType(dataType);
        // string 先写死
        if (DbDataType.STRING.getCode().equals(dataType)) {
            Map<String, Object> map = new HashMap<>(16);
            map.put("length", 1024);
            dt.setSpecs(map);
        }
        return dt;
    }
}
