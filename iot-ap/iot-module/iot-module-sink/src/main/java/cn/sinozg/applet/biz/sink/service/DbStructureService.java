package cn.sinozg.applet.biz.sink.service;

import cn.sinozg.applet.biz.com.vo.request.TmSinkRequest;

/**
 * 数据库结构接口
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-06 13:32
 */
public interface DbStructureService {

    /**
     * 同步模型结构
     * @param info 模型
     * @param create 是否新建
     */
    void syncThingModel (TmSinkRequest info, boolean create);

    /**
     * 初始化其他超级表
     * @param delete 是否删除
     *
     */
    void initDbStructure(boolean delete);
}
