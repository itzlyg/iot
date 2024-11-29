package cn.sinozg.applet.biz.notify.util;

import cn.sinozg.applet.biz.notify.model.AlerterBaseInfo;
import cn.sinozg.applet.biz.notify.model.AlerterTagItemInfo;
import cn.sinozg.applet.biz.notify.model.NotifierAlerterInfo;
import cn.sinozg.applet.common.function.FunctionException;
import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-20 17:07
 */
@Slf4j
public class AlerterUtil {
    /**
     * Match the variable ${key}
     * 匹配 ${key} 的变量
     * eg: Alert, the instance: ${instance} metrics: ${metrics} is over flow.
     */
    private static final Pattern PATTERN = Pattern.compile("\\$\\{(\\w+)}");

    public static final String OS_NAME = System.getProperty("os.name");

    private static final String LINUX = "linux";

    private static final String WINDOWS = "windows";

    private static boolean isLinuxPlatform = false;
    private static boolean isWindowsPlatform = false;

    static {
        if (OS_NAME != null && OS_NAME.toLowerCase().contains(LINUX)) {
            isLinuxPlatform = true;
        }

        if (OS_NAME != null && OS_NAME.toLowerCase().contains(WINDOWS)) {
            isWindowsPlatform = true;
        }
    }

    /**
     * 深复制数据
     * @param t 原始数据
     * @return 复制的数据
     * @param <T> 数据类型
     */
    public static <T> T clone(T t){
        if (t == null) {
            return null;
        }
        Class<T> clazz = PojoUtil.cast(t.getClass());
        return JsonUtil.toPojo(JsonUtil.toJson(t), clazz);
    }



    public static String render(String template, Map<String, Object> replaceData) {
        if (template == null) {
            return null;
        }
        try {
            Matcher matcher = PATTERN.matcher(template);
            StringBuffer builder = new StringBuffer();
            while (matcher.find()) {
                Object objectValue = replaceData.getOrDefault(matcher.group(1), "NullValue");
                String value = objectValue.toString();
                matcher.appendReplacement(builder, Matcher.quoteReplacement(value));
            }
            matcher.appendTail(builder);
            return builder.toString();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return template;
        }
    }

    /**
     * 将字符串str转换为int数字类型
     *
     * @param str string
     * @return double 数字
     */
    public static Integer parseStrInteger(final String str) {
        return parse(str, Integer::parseInt);
    }

    /**
     * 将字符串str转换为double数字类型
     *
     * @param str string
     * @return double 数字
     */
    public static Double parseStrDouble(final String str) {
        return parse(str, Double::parseDouble);
    }

    /**
     * 将时间字符串str转换为秒
     *
     * @param str string
     * @return double 数字
     */
    public static int parseTimeStrToSecond(final String str) {
        LocalTime lt = parse(str, LocalTime::parse);
        if (lt != null) {
            return lt.toSecondOfDay();
        }
        return -1;
    }

    /**
     * 将字符串str,此字符串可能带单位,转换为double数字类型
     * 将数值小数点限制到4位
     *
     * @param str  string
     * @param unit 字符串单位
     * @return string格式的 double 数字 小数点最大到4位
     */
    public static String parseDoubleStr(String str, String unit) {
        if (StringUtils.isBlank(str)) {
            return null;
        }

        try {
            if (unit != null && str.endsWith(unit)) {
                str = str.substring(0, str.length() - unit.length());
            }
            BigDecimal bigDecimal = new BigDecimal(str);
            return bigDecimal.setScale(4, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return null;
        }
    }

    /**
     * whether the running environment is linux
     * @return is linux platform or not
     */
    public static boolean isLinux() {
        return isLinuxPlatform;
    }

    /**
     * whether the running environment is windows
     * @return is windows platform or not
     */
    public static boolean isWindows() {
        return isWindowsPlatform;
    }

    /**
     * 转化
     * @param str 数据
     * @param fun 函数
     * @return 值
     * @param <T> 类型
     */
    public static <T> T parse(String str, FunctionException<String, T> fun){
        try {
            if (StringUtils.isNotBlank(str)) {
                return fun.apply(str);
            }
        } catch (Exception e) {
            log.error("parse 异常！", e);
        }
        return null;
    }

    /**
     * 设置一个集合到bean
     * @param t bean
     * @param r 集合
     * @param get get
     * @param set set
     * @param <T> bean
     * @param <R> 集合
     */
    public static <T, R> void setList(T t, List<R> r, Function<T, List<R>> get, BiConsumer<T, List<R>> set){
        if (t == null || r == null) {
            return;
        }
        List<R> list = get.apply(t);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.addAll(r);
        set.accept(t, list);
    }

    /**
     * 匹配规则
     * @param alerter 规则
     * @param currentAlert 告警
     * @return 是否匹配
     */
    public static boolean matcherReduce(AlerterBaseInfo alerter, NotifierAlerterInfo currentAlert){
        boolean match = alerter.isMatchAll();
        if (!match) {
            List<AlerterTagItemInfo> tagsItem = alerter.getTags();
            if (CollectionUtils.isNotEmpty(tagsItem)) {
                Map<String, String> alertTagMap = currentAlert.getTags();
                for (AlerterTagItemInfo ti : tagsItem) {
                    String tagName = ti.getName();
                    if (alertTagMap.containsKey(tagName)) {
                        String v = alertTagMap.get(tagName);
                        if (StringUtils.equals(v, ti.getValue())) {
                            match = true;
                            break;
                        }
                    }
                }
            } else {
                match = true;
            }
            if (match && CollectionUtils.isNotEmpty(alerter.getPriorities())) {
                if (!CollectionUtils.containsAny(alerter.getPriorities(), currentAlert.getPriority())) {
                    match = false;
                }
            }
        }
        return match;
    }
}
