package cn.sinozg.applet.biz.notify.reduce;

import cn.sinozg.applet.biz.notify.model.AlerterSilenceInfo;
import cn.sinozg.applet.biz.notify.model.NotifierAlerterInfo;
import cn.sinozg.applet.biz.notify.service.AlerterCacheService;
import cn.sinozg.applet.biz.notify.service.AlerterDataService;
import cn.sinozg.applet.biz.notify.util.AlerterUtil;
import cn.sinozg.applet.common.constant.Constants;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


/**
 * 告警静默管理用于您在系统维护期间或夜晚周末不想受到告警打扰时，可以点击”新增静默策略“，设置指定时间段内屏蔽告警通知。
 * 告警静默规则支持一次性时间段或周期性时间段，支持标签匹配和告警级别匹配部分告警
 * @Description
 * @Copyright Copyright (c) 2024
 * @author xieyubin
 * @since 2024-01-20 15:12:21
 */
@Component
@RequiredArgsConstructor
public class AlerterSilenceReduce {

	private final AlerterCacheService cacheService;

	private final AlerterDataService alerterDataService;

	/**
	 * 过滤 场景
	 * @param alert 数据
	 * @return 是否符合
	 */
	public boolean filterSilence(NotifierAlerterInfo alert) {
		List<AlerterSilenceInfo> silenceList = cacheService.silenceCache();
		for (AlerterSilenceInfo silence : silenceList) {
			if (!silence.isEnable()) {
				continue;
			}
			// if match the silence rule, return
			boolean match = AlerterUtil.matcherReduce(silence, alert);
			if (match) {
				// once time
				if (Constants.STATUS_01.equals(silence.getType()) && match(silence, true)) {
					return false;
				} else if (Constants.STATUS_02.equals(silence.getType())) {
					// cyc time
					int currentDayOfWeek = LocalDate.now().getDayOfWeek().getValue();
					List<Integer> days = silence.getDays();
					if (CollectionUtils.isNotEmpty(days)) {
						boolean dayMatch = CollectionUtils.containsAny(days, currentDayOfWeek);
						if (dayMatch && match(silence, false)) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private boolean match (AlerterSilenceInfo silence, boolean isDate){
		LocalDateTime nowDate = LocalDateTime.now();
		boolean startMatch = silence.getPeriodStart() == null;
		boolean endMatch = silence.getPeriodEnd() == null;
		if (isDate) {
			startMatch = startMatch || nowDate.isAfter(silence.getPeriodStart());
			endMatch = endMatch || nowDate.isAfter(silence.getPeriodEnd());
		} else {
			LocalTime nowTime = nowDate.toLocalTime();
			startMatch = startMatch || nowTime.isAfter(silence.getPeriodStart().toLocalTime());
			endMatch = endMatch || nowTime.isAfter(silence.getPeriodEnd().toLocalTime());
		}
		if (startMatch && endMatch) {
			int times = Optional.ofNullable(silence.getAlerterTimes()).orElse(0);
			silence.setAlerterTimes(times + 1);
			alerterDataService.saveSilence(silence);
			return true;
		}
		return false;
	}
}
