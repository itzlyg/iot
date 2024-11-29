package cn.sinozg.applet.biz.notify.handle.impl;

import cn.sinozg.applet.biz.notify.core.AlerterContext;
import cn.sinozg.applet.biz.notify.enums.NotifierType;
import cn.sinozg.applet.biz.notify.handle.BaseNotifierAlerterHandle;
import cn.sinozg.applet.biz.notify.model.NotifierAlerterInfo;
import cn.sinozg.applet.biz.notify.model.NotifierTemplateInfo;
import cn.sinozg.applet.biz.notify.model.channel.NotifierEmailConfig;
import cn.sinozg.applet.biz.notify.model.freemark.FreemarkerModel;
import cn.sinozg.applet.common.constant.BaseConstants;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.common.utils.SpringUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * 邮件通知
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-19 14:55
 */
@Slf4j
@Service
public class EmailNotifierAlerterHandleImpl extends BaseNotifierAlerterHandle<NotifierEmailConfig> {

//    @Resource
//    private JavaMailSender javaMailSender;

    @Override
    public void send(NotifierEmailConfig params, NotifierTemplateInfo notifyMessage, NotifierAlerterInfo alert) {
        JavaMailSender javaMailSender = SpringUtil.getBean(JavaMailSender.class);
        // 获取sender
        JavaMailSenderImpl sender = (JavaMailSenderImpl) javaMailSender;
        sender.setHost(params.getHost());
        sender.setPort(params.getPort());
        sender.setUsername(params.getUserName());
        sender.setPassword(params.getPassword());
        Properties props = sender.getJavaMailProperties();
        props.put("mail.smtp.ssl.enable", params.isSslEnable());
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, BaseConstants.UTF8);
            messageHelper.setSubject(AlerterContext.TITLE);
            // Set sender Email 设置发件人Email
            messageHelper.setFrom(params.getFromUser());
            // Set recipient Email 设定收件人Email
            messageHelper.setTo(PojoUtil.toArray(alert.getToUser(), String.class));
            messageHelper.setSentDate(new Date());
            // Build email templates 构建邮件模版
            String process = buildAlertHtmlTemplate(notifyMessage, alert);
            // Set Email Content Template 设置邮件内容模版
            messageHelper.setText(process, true);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new CavException("发送邮件错误！", e);
        }
    }

    @Override
    public NotifierType notifyType() {
        return NotifierType.EMAIL;
    }

    private String buildAlertHtmlTemplate (NotifierTemplateInfo templateInfo, NotifierAlerterInfo alert) throws IOException, TemplateException {
        FreemarkerModel model = new FreemarkerModel();
        model.setUrl(properties.getConsoleUrl());
        return freemarkerTemplate(model, templateInfo, alert, "mailTemplate");
    }
}
