package cn.sinozg.applet.biz.notify.execute;

import cn.sinozg.applet.biz.notify.model.NotifierRuleInfo;
import cn.sinozg.applet.common.constant.RedisKey;
import cn.sinozg.applet.common.utils.RedisUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-24 20:39
 */
public class AlerterCache {

    public static List<NotifierRuleInfo> ruleInfo(){
        List<NotifierRuleInfo> rules = RedisUtil.getCacheObject(RedisKey.ALERTER_RULE);
        if (CollectionUtils.isEmpty(rules)) {
            List<NotifierRuleInfo> dbs = new ArrayList<>();
            NotifierRuleInfo dbRule = new NotifierRuleInfo();
            setRule(dbRule);
        }
        return rules;
    }

    public static void setRule(NotifierRuleInfo rule){
        RedisUtil.setCacheObject(RedisKey.ALERTER_RULE, rule);
    }
}
