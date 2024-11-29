package cn.sinozg.applet.biz.notify.execute;

import cn.sinozg.applet.biz.notify.enums.NotifierType;
import cn.sinozg.applet.biz.notify.handle.NotifierAlerterHandle;
import cn.sinozg.applet.biz.notify.model.NotifierAlerterInfo;
import cn.sinozg.applet.biz.notify.model.NotifierTemplateInfo;
import cn.sinozg.applet.biz.notify.model.channel.NotifierConfigBaseInfo;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.common.utils.SpringUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

/**
 * @Author: xyb
 * @Description:
 * @Date: 2023-12-30 下午 01:11
 **/
@Slf4j
public class AlerterFactory {

    private static final Map<String, NotifierTemplateInfo> PRESET_TEMPLATE = new HashMap<>(16);
    private static final Map<NotifierType, NotifierAlerterHandle<? extends NotifierConfigBaseInfo>> HANDLE_MAP = new HashMap<>();

    /**
     * 初始化模板
     * @param list 模板
     */
    public static void initMap (List<NotifierTemplateInfo> list){
        if (CollectionUtils.isNotEmpty(list) && MapUtils.isEmpty(PRESET_TEMPLATE)) {
            list.forEach(t -> PRESET_TEMPLATE.put(t.getType(), t));
        }
    }

    /**
     * 所有模板
     * @return 模板
     */
    public static Map<String, NotifierTemplateInfo> allTemplate (){
        return PRESET_TEMPLATE;
    }

    /**
     * 获取到 模版
     * @param type 类型
     * @return 目标
     */
    public static NotifierTemplateInfo templateByType (NotifierType type){
        if (type == null) {
            return null;
        }
        return PRESET_TEMPLATE.get(type.getCode());
    }

    /**
     *
     * @param params 配置参数
     * @param templateInfo 模板信息
     * @param alerter 预警
     * @return 是否成功
     * @param <T> 类型
     */
    public static <T extends NotifierConfigBaseInfo> boolean sendNoticeMsg(T params, NotifierTemplateInfo templateInfo, NotifierAlerterInfo alerter) {
        if (params == null || params.getType() == null) {
            log.warn("请求参数为空！{}, {}", params, alerter);
            return false;
        }
        NotifierAlerterHandle<?> h = SpringUtil.beanOfType(null, NotifierAlerterHandle.class, HANDLE_MAP, NotifierAlerterHandle::notifyType, n -> params.getType().equals(n.getCode()));
        if (h != null) {
            NotifierAlerterHandle<T> handle = PojoUtil.cast(h);
            handle.send(params, templateInfo, alerter);
            return true;
        }
        return false;
    }
}