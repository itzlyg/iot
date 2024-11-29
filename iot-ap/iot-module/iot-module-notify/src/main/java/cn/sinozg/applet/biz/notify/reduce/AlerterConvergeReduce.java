package cn.sinozg.applet.biz.notify.reduce;

import cn.sinozg.applet.biz.notify.core.TagsContext;
import cn.sinozg.applet.biz.notify.model.AlerterConvergeInfo;
import cn.sinozg.applet.biz.notify.model.NotifierAlerterInfo;
import cn.sinozg.applet.biz.notify.service.AlerterCacheService;
import cn.sinozg.applet.biz.notify.util.AlerterUtil;
import cn.sinozg.applet.common.constant.Constants;
import cn.sinozg.applet.common.utils.DateUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  告警收敛支持对指定时间段内的相同重复告警消息进行去重收敛。您可以点击”新增收敛策略“，并进行配置。
 *  当阈值规则触发告警后，会进入到告警收敛，告警收敛会根据收敛规则对重复告警收敛，以避免大量重复告警消息导致接收风暴。
 *  即多少秒内 避免重复发送
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-20 13:22
 */
@Component
public class AlerterConvergeReduce {

    private final AlerterCacheService cacheService;
    private final Map<Integer, NotifierAlerterInfo> converageAlertMap;
    public AlerterConvergeReduce(AlerterCacheService service){
        this.cacheService = service;
        this.converageAlertMap = new ConcurrentHashMap<>();
    }

    /**
     * 过滤条件
     * @param currentAlert 条件
     * @return 是否适合
     */
    public boolean filterConverge(NotifierAlerterInfo currentAlert) {
        // 忽略
        Map<String, String> tags = currentAlert.getTags();
        if (tags == null || tags.containsKey(TagsContext.IGNORE)) {
            return true;
        }
        List<String> tagKey = new ArrayList<>();
        List<String> tagValue = new ArrayList<>();
        tags.forEach((k, v) -> {
            tagKey.add(k);
            tagValue.add(v);
        });
        // 恢复告警
        if (Constants.STATUS_02.equals(currentAlert.getDataStatus())) {
            // 中-critical-严重告警-橙色
            int alertHash = toHash(Constants.STATUS_02, tagKey, tagValue);
            converageAlertMap.remove(alertHash);
            // 紧急告警
            alertHash = toHash(Constants.STATUS_01, tagKey, tagValue);
            converageAlertMap.remove(alertHash);
            alertHash = toHash(Constants.STATUS_03, tagKey, tagValue);;
            converageAlertMap.remove(alertHash);
            return true;
        }
        List<AlerterConvergeInfo> alertConvergeList = cacheService.convergeCache();
        for (AlerterConvergeInfo converge : alertConvergeList) {
            if (!converge.isEnable()) {
                continue;
            }
            boolean match = AlerterUtil.matcherReduce(converge, currentAlert);
            if (match) {
                long evalInterval = converge.getEvalInterval() * 1000;
                if (evalInterval <= 0) {
                    return true;
                }
                LocalDateTime now = LocalDateTime.now();
                int alertHash = toHash(currentAlert.getPriority(), tagKey, tagValue );
                // 上一个
                NotifierAlerterInfo preAlert = converageAlertMap.get(alertHash);
                if (preAlert == null) {
                    currentAlert.setAlerterTimes(1);
                    currentAlert.setFirstAlerterTime(now);
                    currentAlert.setAlerterTime(now);

                    converageAlertMap.put(alertHash, AlerterUtil.clone(currentAlert));
                    return true;
                } else {
                    if (DateUtil.diffTime(now, preAlert.getFirstAlerterTime()) < evalInterval) {
                        preAlert.setAlerterTimes(preAlert.getAlerterTimes() + 1);
                        preAlert.setAlerterTime(now);
                        return false;
                    } else {
                        currentAlert.setAlerterTimes(preAlert.getAlerterTimes());
                        if (preAlert.getAlerterTimes() == 1) {
                            currentAlert.setFirstAlerterTime(now);
                        } else {
                            currentAlert.setFirstAlerterTime(preAlert.getFirstAlerterTime());
                        }
                        currentAlert.setAlerterTime(now);
                        preAlert.setFirstAlerterTime(now);
                        preAlert.setAlerterTime(now);
                        preAlert.setAlerterTimes(1);
                        return true;
                    }
                }
            }
        }
        return true;
    }
    private int toHash (String priority, List<String> tagKey, List<String> tagValue){
        return Objects.hash(priority) + Arrays.hashCode(PojoUtil.toArray(tagKey, String.class))
                + Arrays.hashCode(PojoUtil.toArray(tagValue, String.class)) ;
    }
}
