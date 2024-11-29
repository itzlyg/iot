package cn.sinozg.applet.biz.notify.service;

import cn.sinozg.applet.biz.notify.model.AlerterDefineInfo;
import cn.sinozg.applet.biz.notify.model.AlerterSilenceInfo;
import cn.sinozg.applet.biz.notify.model.AlerterTagItemInfo;
import cn.sinozg.applet.biz.notify.model.NoticeDefineInfo;
import cn.sinozg.applet.biz.notify.model.NotifierAlerterInfo;
import cn.sinozg.applet.biz.notify.model.NotifierRuleInfo;
import cn.sinozg.applet.biz.notify.model.NotifierTemplateInfo;
import cn.sinozg.applet.biz.notify.model.channel.NotifierConfigBaseInfo;
import cn.sinozg.applet.biz.notify.model.send.SendNoticeRule;

import java.util.List;
import java.util.Map;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-20 14:33
 */
public interface AlerterDataService {

    void saveSilence(AlerterSilenceInfo silence);

    void saveAlerter(NotifierAlerterInfo alerterInfo);

    List<AlerterTagItemInfo> findMonitorIdBindTags(String monitorId);

    Map<String, List<AlerterDefineInfo>> monitorBindAlerterDefines(String monitorId, String app, String metrics);

    AlerterDefineInfo monitorBindAlerterAvaDefine (String monitorId, String app, String metrics);

    List<NotifierAlerterInfo> alerterLike (String tagsLike, String priority, String status, String leAlerterTime);

    void updateAlerterStatus (String dataStatus, List<String> list);

    NotifierConfigBaseInfo configById(String id);

    NotifierTemplateInfo templateById(String id);

    List<NotifierRuleInfo>  matchRulesByAlerter(NotifierAlerterInfo alerterInfo);

    Map<String, SendNoticeRule> noticeRuleByDefId (NoticeDefineInfo define);
}
