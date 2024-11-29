package cn.sinozg.applet.biz.notify.reduce;

import cn.sinozg.applet.biz.notify.core.TagsContext;
import cn.sinozg.applet.biz.notify.model.AlerterTagItemInfo;
import cn.sinozg.applet.biz.notify.model.NotifierAlerterInfo;
import cn.sinozg.applet.biz.notify.service.AlerterDataQueueService;
import cn.sinozg.applet.biz.notify.service.AlerterDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Description 
 * @Copyright Copyright (c) 2024
 * @author xieyubin
 * @since 2024-01-20 14:44:56
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlerterCommonReduce {
	
	private final AlerterSilenceReduce alerterSilenceReduce;
	
	private final AlerterConvergeReduce alerterConvergeReduce;

	private final AlerterDataService alerterDataService;

	private final AlerterDataQueueService alerterDataQueueService;
	
    public void reduceAndSendAlarm(NotifierAlerterInfo alert) {
		alert.setAlerterTimes(1);
	    Map<String, String> tags = alert.getTags();
		if (tags == null) {
			tags = new HashMap<>(16);
			alert.setTags(tags);
		}
	    String monitorId = tags.get(TagsContext.TAG_MONITOR_ID);
	    if (StringUtils.isBlank(monitorId)) {
            log.debug("receiver extern alarm message: {}", alert);
	    } else {
            List<AlerterTagItemInfo> tagList = alerterDataService.findMonitorIdBindTags(monitorId);
			for (AlerterTagItemInfo tag : tagList) {
				if (!tags.containsKey(tag.getName())) {
					tags.put(tag.getName(), tag.getValue());
				}
			}
        }
		// converge -> silence
	    if (alerterConvergeReduce.filterConverge(alert) && alerterSilenceReduce.filterSilence(alert)) {
			alerterDataQueueService.sendAlerterData(alert);
	    }
    }
}
