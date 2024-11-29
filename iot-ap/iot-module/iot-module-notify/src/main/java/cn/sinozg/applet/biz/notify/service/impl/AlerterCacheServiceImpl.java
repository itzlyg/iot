package cn.sinozg.applet.biz.notify.service.impl;

import cn.sinozg.applet.biz.notify.model.AlerterConvergeInfo;
import cn.sinozg.applet.biz.notify.model.AlerterSilenceInfo;
import cn.sinozg.applet.biz.notify.service.AlerterCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-20 13:44
 */
@Slf4j
@Service
public class AlerterCacheServiceImpl implements AlerterCacheService {
    @Override
    public List<AlerterConvergeInfo> convergeCache() {
        return null;
    }

    @Override
    public List<AlerterSilenceInfo> silenceCache() {
        return null;
    }
}
