package cn.sinozg.applet.biz.notify.service;

import cn.sinozg.applet.biz.notify.model.AlerterConvergeInfo;
import cn.sinozg.applet.biz.notify.model.AlerterSilenceInfo;

import java.util.List;

/**
 * 缓存 业务实现
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-20 13:41
 */
public interface AlerterCacheService {

    List<AlerterConvergeInfo> convergeCache ();

    List<AlerterSilenceInfo> silenceCache();
}
