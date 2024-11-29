package cn.sinozg.applet.biz.notify.service.impl;

import cn.sinozg.applet.biz.notify.core.AlerterContext;
import cn.sinozg.applet.biz.notify.execute.AlerterFactory;
import cn.sinozg.applet.biz.notify.model.NotifierTemplateInfo;
import cn.sinozg.applet.biz.notify.service.AlerterDataQueueService;
import cn.sinozg.applet.biz.notify.service.AlerterDataService;
import cn.sinozg.applet.biz.notify.timer.AlerterTask;
import cn.sinozg.applet.biz.notify.util.AlerterThread;
import cn.sinozg.applet.common.constant.BaseConstants;
import cn.sinozg.applet.common.service.FrameworkInitRunService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 初始化 静态资源
 * 初始化 线程 获取预警分发
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-19 16:25
 */
@Slf4j
@Service
public class NotifierAlerterInitServiceImpl implements FrameworkInitRunService {

    @Resource
    private AlerterDataService alerterDataService;

    @Resource
    private AlerterDataQueueService dataQueueService;

    @Override
    public void initInfo() {
        initStatusTemplate();
        alerterJob();
    }

    private void alerterJob (){
        // 启动报警分发
        AlerterTask task = new AlerterTask(dataQueueService, alerterDataService);
        for (int i = 0; i < AlerterContext.DISPATCH_THREADS; i++) {
            AlerterThread.execute(task);
        }
    }

    /**
     * 初始化静态模版
     */
    private void initStatusTemplate (){
        try {
            log.info("load default notice template in internal jar");
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            org.springframework.core.io.Resource[] resources = resolver.getResources("classpath:alerter/*.*");
            List<NotifierTemplateInfo> list = new ArrayList<>();
            for (org.springframework.core.io.Resource resource : resources) {
                String fileName = resource.getFilename();
                if (resource.getFilename() == null) {
                    log.warn("Ignore the template file {}.", resource.getFilename());
                    continue;
                }
                try (InputStream inputStream = resource.getInputStream()) {
                    String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                    NotifierTemplateInfo template = new NotifierTemplateInfo();
                    String name = StringUtils.substringBeforeLast(fileName, BaseConstants.SPOT);
                    if (StringUtils.isBlank(name)) {
                        log.warn("模板文件名称不符合格式 {}.", fileName);
                        continue;
                    }
                    String[] names = name.split(BaseConstants.MIDDLE_LINE);
                    if (names.length != 2) {
                        log.warn("模板文件名称不符合格式 {}.", fileName);
                        continue;
                    }
                    template.setName(names[1]);
                    template.setType(names[0]);
                    template.setPreset(true);
                    template.setContent(content);
                    list.add(template);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                    log.error("Ignore this template file: {}.", resource.getFilename());
                }
            }
            AlerterFactory.initMap(list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
