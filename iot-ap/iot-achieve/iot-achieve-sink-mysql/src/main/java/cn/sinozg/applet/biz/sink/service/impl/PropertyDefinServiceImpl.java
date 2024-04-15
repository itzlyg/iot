package cn.sinozg.applet.biz.sink.service.impl;

import cn.sinozg.applet.biz.com.model.TmBaseDataTypeInfo;
import cn.sinozg.applet.biz.com.model.TmDataTypeInfo;
import cn.sinozg.applet.biz.com.vo.request.TmSinkRequest;
import cn.sinozg.applet.biz.sink.entity.PropertyDefin;
import cn.sinozg.applet.biz.sink.mapper.PropertyDefinMapper;
import cn.sinozg.applet.biz.sink.service.PropertyDefinService;
import cn.sinozg.applet.common.utils.JsonUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* 产品属性定义表 服务实现类
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-03-07 12:38:11
*/
@Service
public class PropertyDefinServiceImpl extends ServiceImpl<PropertyDefinMapper, PropertyDefin> implements PropertyDefinService {

    @Resource
    private PropertyDefinMapper mapper;
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void definitionProperty(TmSinkRequest params) {
        String prodKey = params.getProdKey();
        // 删除原始数据
        this.remove(new LambdaQueryWrapper<PropertyDefin>().eq(PropertyDefin::getProdKey, prodKey));
        List<PropertyDefin> list = new ArrayList<>();
        List<TmBaseDataTypeInfo> attrs = params.getAttributes();
        for (TmBaseDataTypeInfo attr : attrs) {
            PropertyDefin defin = new PropertyDefin();
            defin.setProdKey(prodKey);
            defin.setIdentifier(attr.getIdentifier());
            TmDataTypeInfo type = attr.getDataType();
            if (type != null) {
                defin.setType(type.getType());
                if (type.getSpecs() != null) {
                    defin.setSpecs(JsonUtil.toJson(type.getSpecs()));
                }
            }
            list.add(defin);
        }
        this.saveBatch(list);
    }
}
