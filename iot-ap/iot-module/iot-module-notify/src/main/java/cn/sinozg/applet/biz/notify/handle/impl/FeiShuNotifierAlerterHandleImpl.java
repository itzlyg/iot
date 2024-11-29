package cn.sinozg.applet.biz.notify.handle.impl;

import cn.sinozg.applet.biz.notify.core.AlerterContext;
import cn.sinozg.applet.biz.notify.enums.NotifierType;
import cn.sinozg.applet.biz.notify.handle.BaseNotifierAlerterHandle;
import cn.sinozg.applet.biz.notify.model.NotifierAlerterInfo;
import cn.sinozg.applet.biz.notify.model.NotifierTemplateInfo;
import cn.sinozg.applet.biz.notify.model.channel.NotifierFeiShuConfig;
import cn.sinozg.applet.biz.notify.model.feishu.FsContent;
import cn.sinozg.applet.biz.notify.model.feishu.FsContentDetail;
import cn.sinozg.applet.biz.notify.model.feishu.FsLanguage;
import cn.sinozg.applet.biz.notify.model.feishu.FsModel;
import cn.sinozg.applet.biz.notify.model.feishu.FsPost;
import cn.sinozg.applet.biz.notify.model.response.RobotNotifyResponse;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 飞书发送消息
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-19 17:5
 */
@Slf4j
@Service
public class FeiShuNotifierAlerterHandleImpl extends BaseNotifierAlerterHandle<NotifierFeiShuConfig> {
    @Override
    public void send(NotifierFeiShuConfig params, NotifierTemplateInfo notifyTemplate, NotifierAlerterInfo alert) {

        FsModel fsModel = new FsModel();
        FsContent content = new FsContent();
        FsPost post = new FsPost();

        List<List<FsContentDetail>> contents = new ArrayList<>();
        List<FsContentDetail> list = new ArrayList<>();

        FsContentDetail a = new FsContentDetail();
        a.setTag("a");
        a.setText(AlerterContext.LABEL_CONSOLE);
        a.setHref(properties.getConsoleUrl());
        list.add(a);

        FsContentDetail text = new FsContentDetail();
        text.setTag("text");
        text.setText(renderContent(notifyTemplate, alert));
        list.add(text);
        contents.add(list);

        FsLanguage language = new FsLanguage();
        language.setTitle("[" + AlerterContext.TITLE + "]");
        language.setContent(contents);
        post.setLanguage(language);
        content.setPost(post);

        fsModel.setContent(content);

        RobotNotifyResponse response = HttpUtil.doPost(properties.getFeiShuUrl() + params.getOpenId(), null, fsModel, RobotNotifyResponse.class);
        if (response == null || response.getCode() != 0) {
            throw new CavException("发送飞书消息失败！");
        }
    }

    @Override
    public NotifierType notifyType() {
        return NotifierType.FEI_SHU;
    }
}
