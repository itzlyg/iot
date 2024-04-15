package cn.sinozg.applet.biz.system.service.impl;

import cn.sinozg.applet.biz.system.service.CorpInfoService;
import cn.sinozg.applet.biz.system.service.DictDataService;
import cn.sinozg.applet.common.core.model.DictDataVo;
import cn.sinozg.applet.common.enums.DictType;
import cn.sinozg.applet.common.service.FrameworkInitDataService;
import cn.sinozg.applet.common.utils.DictUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 需要启动后加载到系统缓存的方法
 * @Author: xyb
 * @Description:
 * @Date: 2023-03-24 下午 01:12
 **/
@Slf4j
@Service
public class FrameworkInitDataServiceImpl implements FrameworkInitDataService {
    @Resource
    private DictDataService dictDataService;

    @Resource
    private CorpInfoService corpService;

    /**
     * 需要初始化到缓存的接口实现
     *  还会定时刷新
     */
    @Override
    public void cacheSystemData() {
        Map<String, List<DictDataVo>> data = new HashMap<>(1024);
        Map<String, List<DictDataVo>> dictDataMap = dictDataService.allDictCache();
        data.putAll(dictDataMap);
        log.info("加载客户的数据字典信息！！");
        List<DictDataVo> corps = corpService.corpList();
        if (CollectionUtils.isNotEmpty(corps)) {
            data.put(DictType.TENANT.getCode(), corps);
            log.info("加载租户信息完成！！");
        }
        DictUtil.setDictCache(data);
        log.info("缓存标准数据成功！");
    }

    /**
     * 只需要初始化的系统信息
     */
    @Override
    public void initSystemInfo() {
    }

    @Override
    public void postConstruct() {

    }

    @Override
    public void addCustomInterceptor(InterceptorRegistry registry) {

    }
}