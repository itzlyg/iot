package cn.sinozg.applet.turbo.engine.config;

import cn.sinozg.applet.common.utils.JsonUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: james zhangxiao
 * @Date: 11/30/22
 * @Description:
 */
@Component
public class BusinessConfig {

    @Value("${callActivity.nested.level:#{null}}")
    private String callActivityNestedLevel;
    // computing flow nested level
    public static final int COMPUTING_FLOW_NESTED_LEVEL = -1;
    // Flow don't use CallActivity node
    public static final int MIN_FLOW_NESTED_LEVEL = 0;
    public static final int MAX_FLOW_NESTED_LEVEL = 10;

    /**
     * Query callActivityNestedLevel according to caller
     * <p>
     * e.g.1 if flowA quote flowB, flowA callActivityNestedLevel equal to 1.
     * e.g.2 if flowA quote flowB, flowB quote flowC, flowA callActivityNestedLevel equal to 2.
     *
     * @param caller caller
     * @return -1 if unLimited
     */
    public int getCallActivityNestedLevel(String caller) {
        if (StringUtils.isBlank(callActivityNestedLevel)) {
            return MAX_FLOW_NESTED_LEVEL;
        }
        Map<String, ?> map = JsonUtil.toMap(callActivityNestedLevel);
        int config = MapUtils.getIntValue(map, caller, MAX_FLOW_NESTED_LEVEL);
        return Math.min(config, MAX_FLOW_NESTED_LEVEL);
    }
}
