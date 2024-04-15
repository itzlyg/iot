package cn.sinozg.applet.biz.sink.service;

import cn.sinozg.applet.biz.com.vo.request.TmSinkRequest;
import cn.sinozg.applet.biz.sink.entity.PropertyDefin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* 产品属性定义表 服务类
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-03-07 12:38:11
*/
public interface PropertyDefinService extends IService<PropertyDefin> {
    void definitionProperty(TmSinkRequest params);
}
