package cn.sinozg.applet.biz.notify.handle;

import cn.sinozg.applet.biz.notify.core.TagsContext;
import cn.sinozg.applet.biz.notify.enums.NotifierType;
import cn.sinozg.applet.biz.notify.execute.AlerterFactory;
import cn.sinozg.applet.biz.notify.model.NotifierAlerterInfo;
import cn.sinozg.applet.biz.notify.model.NotifierTemplateInfo;
import cn.sinozg.applet.biz.notify.model.channel.NotifierConfigBaseInfo;
import cn.sinozg.applet.biz.notify.model.freemark.FreemarkerModel;
import cn.sinozg.applet.biz.notify.properties.AlerterProperties;
import cn.sinozg.applet.common.constant.BaseConstants;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.DateUtil;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

/**
 * @Author: xyb
 * @Description:
 * @Date: 2023-12-30 下午 01:04
 **/
@Slf4j
public abstract class BaseNotifierAlerterHandle<T extends NotifierConfigBaseInfo> implements NotifierAlerterHandle<T> {

    @Resource
    protected AlerterProperties properties;

    /**
     * 转化模板内容，生成文本
     * 模版为空的时候获取到默认的静态模板
     * @param noticeTemplate 模版
     * @param alert 通知定义
     * @return 内容
     */
    protected String renderContent(NotifierTemplateInfo noticeTemplate, NotifierAlerterInfo alert) {
        FreemarkerModel model = new FreemarkerModel();
        model.setStatus(alert.getDataStatus());
        model.setAlerterTimes(alert.getAlerterTimes());
        model.setTags(alert.getTags());
        try {
            String template = freemarkerTemplate(model, noticeTemplate, alert, "freeMakerTemplate");
            return template.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1");
        } catch (Exception e) {
            log.error("获取通知告警文本错误！", e);
            throw new CavException("获取{}通知告警文本错误！", notifyType().getName());
        }
    }


    protected String freemarkerTemplate (FreemarkerModel model, NotifierTemplateInfo noticeTemplate,
                                         NotifierAlerterInfo alert, String templateName) throws IOException, TemplateException {
        NotifierType notifyType = notifyType();
        if (notifyType == null) {
            throw new CavException("未定义的发送类型！");
        }
        Template templateMail;
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        cfg.setNumberFormat("0");
        String monitorId = null;
        String monitorName = null;
        Map<String, String> tags = alert.getTags();
        if (MapUtils.isNotEmpty(tags)) {
            monitorId = MapUtils.getString(tags, TagsContext.TAG_MONITOR_ID);
            monitorName = MapUtils.getString(tags, TagsContext.TAG_MONITOR_NAME);
        }
        model.setMonitorId(StringUtils.defaultString(monitorId, "No ID"));
        model.setMonitorName(StringUtils.defaultString(monitorName, "No Name"));
        model.setTarget(alert.getTarget());
        model.setPriority(alert.getPriorityName());
        model.setTriggerTime(DateUtil.formatDateTime(alert.getAlerterTime(), null));
        if (BaseConstants.STATUS_02.equals(alert.getDataStatus())) {
            model.setRestoreTime(DateUtil.formatDateTime(alert.getRestoreTime(), null));
        } else {
            model.setLabelRestoreTime(null);
        }
        model.setContent(alert.getContent());
        if (noticeTemplate == null) {
            noticeTemplate = AlerterFactory.templateByType(notifyType);
        }
        if (noticeTemplate == null) {
            throw new CavException("{}未设置默认模版！", notifyType.getName());
        }
        // TODO 单实例复用缓存 考虑多线程问题
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        stringLoader.putTemplate(templateName, noticeTemplate.getContent());
        cfg.setTemplateLoader(stringLoader);
        templateMail = cfg.getTemplate(templateName, Locale.CHINESE);
        return FreeMarkerTemplateUtils.processTemplateIntoString(templateMail, model);
    }
}