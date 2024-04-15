package cn.sinozg.applet.biz.sink.service.impl;

import cn.sinozg.applet.biz.com.vo.request.TmSinkRequest;
import cn.sinozg.applet.biz.sink.service.DbStructureService;
import cn.sinozg.applet.biz.sink.service.PropertyDefinService;
import cn.sinozg.applet.common.exception.CavException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-06 13:38
 */
@Slf4j
@Service
public class DbStructureServiceImpl implements DbStructureService {

    @Resource
    private PropertyDefinService definService;

    @Override
    public void syncThingModel(TmSinkRequest info, boolean create) {
        if (CollectionUtils.isEmpty(info.getAttributes())) {
            throw new CavException("请定义产品属性！");
        }
        definService.definitionProperty(info);
    }

    @Override
    public void initDbStructure(boolean delete) {
    }
}
