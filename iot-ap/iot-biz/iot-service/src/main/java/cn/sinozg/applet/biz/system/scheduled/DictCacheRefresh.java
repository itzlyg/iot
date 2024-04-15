package cn.sinozg.applet.biz.system.scheduled;

import cn.sinozg.applet.common.runner.BaseScheduling;
import cn.sinozg.applet.common.service.FrameworkInitDataService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 刷新标准数据
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-09-13 15:26
 */
@Component
public class DictCacheRefresh extends BaseScheduling {

    @Resource
    private FrameworkInitDataService service;

    @Override
    public void setParams() {
        this.cron = "0 0/10 * * * ?";
        this.clazz = DictCacheRefresh.class;
    }

    @Override
    public void execute() {
        service.cacheSystemData();
    }
}
