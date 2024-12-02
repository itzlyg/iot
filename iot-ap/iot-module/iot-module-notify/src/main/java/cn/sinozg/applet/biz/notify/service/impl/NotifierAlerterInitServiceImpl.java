package cn.sinozg.applet.biz.notify.service.impl;

import cn.sinozg.applet.biz.notify.core.AlerterContext;
import cn.sinozg.applet.biz.notify.execute.AlerterFactory;
import cn.sinozg.applet.biz.notify.model.NotifierTemplateInfo;
import cn.sinozg.applet.biz.notify.service.AlerterDataQueueService;
import cn.sinozg.applet.biz.notify.service.AlerterDataService;
import cn.sinozg.applet.biz.notify.timer.AlerterTask;
import cn.sinozg.applet.biz.notify.util.AlerterThread;
import cn.sinozg.applet.common.service.FrameworkInitRunService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static final Pattern FILE_NAME_PATTERN = Pattern.compile("[0-9]+-[a-zA-Z]+.[a-zA-Z]+");

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
                if (fileName == null) {
                    log.warn("Ignore the template file {}.", resource.getFilename());
                    continue;
                }
                Matcher m = FILE_NAME_PATTERN.matcher(fileName);
                if (!m.find()) {
                    log.warn("模板文件名称不符合格式 {}.", fileName);
                    continue;
                }
                try (InputStream inputStream = resource.getInputStream()) {
                    String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                    NotifierTemplateInfo template = new NotifierTemplateInfo();
                    template.setName(m.group(2));
                    template.setType(m.group(1));
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
