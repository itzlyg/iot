package cn.sinozg.applet.biz.notify.util;

import cn.sinozg.applet.biz.notify.core.AlerterContext;
import cn.sinozg.applet.biz.notify.model.collect.Configmap;
import cn.sinozg.applet.biz.notify.model.collect.MetricsField;
import cn.sinozg.applet.biz.notify.model.collect.MetricsInfo;
import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 * @Description 
 * @Copyright Copyright (c) 2024
 * @author xieyubin
 * @since 2024-01-22 12:50:50
 */
@Slf4j
public class CollectUtil {

    private static final int DEFAULT_TIMEOUT = 60000;
    private static final int HEX_STR_WIDTH = 2;
    private static final String SMILING_PLACEHOLDER = "^_^";
    private static final String SMILING_PLACEHOLDER_REX = "\\^_\\^";
    private static final String SMILING_PLACEHOLDER_REGEX = "(\\^_\\^)(\\w|-|$|\\.)+(\\^_\\^)";
    private static final Pattern SMILING_PLACEHOLDER_REGEX_PATTERN = Pattern.compile(SMILING_PLACEHOLDER_REGEX);
    private static final String CRYING_PLACEHOLDER_REX = "\\^o\\^";
    private static final String CRYING_PLACEHOLDER_REGEX = "(\\^o\\^)(\\w|-|$|\\.)+(\\^o\\^)";
    private static final Pattern CRYING_PLACEHOLDER_REGEX_PATTERN = Pattern.compile(CRYING_PLACEHOLDER_REGEX);
    private static final List<String> UNIT_SYMBOLS = Arrays.asList("%", "G", "g", "M", "m", "K", "k", "B", "b");

    /**
     * count match keyword number
     *
     * @param content content
     * @param keyword keyword
     * @return match num
     */
    public static int countMatchKeyword(String content, String keyword) {
        if (StringUtils.isAnyBlank(content, keyword)) {
            return 0;
        }
        try {
            Pattern pattern = Pattern.compile(keyword);
            Matcher matcher = pattern.matcher(content);
            int count = 0;
            while (matcher.find()) {
                count++;
            }
            return count;
        } catch (Exception e) {
            return 0;
        }
    }

    public static ImmutablePair<Double, String> extractDoubleAndUnitFromStr(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        try {
            Double doubleValue = Double.parseDouble(str);
            return ImmutablePair.of(doubleValue, null);
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        // extract unit from str value, eg: 23.43GB, 33KB, 44.22G
        try {
            // B KB MB GB % ....
            for (String unitSymbol : UNIT_SYMBOLS) {
                int index = str.indexOf(unitSymbol);
                if (index == 0) {
                    Double doubleValue = 0d;
                    String unit = str.trim();
                    return ImmutablePair.of(doubleValue, unit);
                }
                if (index > 0) {
                    Double doubleValue = Double.parseDouble(str.substring(0, index));
                    String unit = str.substring(index).trim();
                    return ImmutablePair.of(doubleValue, unit);
                }
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * get timeout integer
     *
     * @param timeout timeout str
     * @return timeout
     */
    public static int getTimeout(String timeout) {
        return getTimeout(timeout, DEFAULT_TIMEOUT);
    }

    /**
     * get timeout integer or default value
     *
     * @param timeout        timeout str
     * @param defaultTimeout default timeout
     * @return timeout
     */
    public static int getTimeout(String timeout, int defaultTimeout) {
        Double d = AlerterUtil.parseStrDouble(timeout);
        if (d != null) {
            return d.intValue();
        }
        return defaultTimeout;
    }

    /**
     * assert prom field
     */
    public static Boolean assertPromRequireField(String aliasField) {
        return StringUtils.equalsAny(aliasField, AlerterContext.PROM_TIME, AlerterContext.PROM_VALUE);
    }


    /**
     * is contains cryPlaceholder ^o^xxx^o^
     *
     * @param jsonNode json Node
     * @return return true when contains
     */
    public static boolean containCryPlaceholder(JsonNode jsonNode) {
        String jsonStr = JsonUtil.toJson(jsonNode);
        return CRYING_PLACEHOLDER_REGEX_PATTERN.matcher(jsonStr).find();
    }

    public static boolean notContainCryPlaceholder(JsonNode jsonNode) {
        return !containCryPlaceholder(jsonNode);
    }

    /**
     * match existed cry placeholder fields ^o^field^o^
     * @param jsonNode json Node
     * @return match field str
     */
    public static Set<String> matchCryPlaceholderField(JsonNode jsonNode) {
        String jsonStr = JsonUtil.toJson(jsonNode);
        Set<String> set = new HashSet<>();
        Matcher matcher = CRYING_PLACEHOLDER_REGEX_PATTERN.matcher(jsonStr);
        while (matcher.find()) {
            String group = matcher.group();
            set.add(group.replaceAll(CRYING_PLACEHOLDER_REX, StringUtils.EMPTY));
        }
        return set;
    }

    public static MetricsInfo replaceCryPlaceholder(MetricsInfo metricItem, Map<String, Configmap> configmap) {
        JsonNode metricNode = JsonUtil.toNode(metricItem);
        replaceCryPlaceholder(metricNode, configmap);
        return JsonUtil.nodeBean(metricNode, MetricsInfo.class);
    }

    /**
     * json parameter replacement
     *
     * @param jsonNode json
     * @param configmap   parameter map
     * @return json
     */
    private static JsonNode replaceCryPlaceholder(JsonNode jsonNode, Map<String, Configmap> configmap) {
        if (jsonNode.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> nodes = jsonNode.fields();
            while (nodes.hasNext()) {
                Map.Entry<String, JsonNode> entry = nodes.next();
                ObjectNode on = (ObjectNode) jsonNode;
                ImmutablePair<String, List<String>> result = matcherValue(entry.getValue(), false, false, configmap);
                if (result == null) {
                    on.set(entry.getKey(), replaceCryPlaceholder(entry.getValue(), configmap));
                } else {
                    String value = result.getKey();
                    if (StringUtils.isNotBlank(value)) {
                        on.put(entry.getKey(), value);
                    }
                }
            }
        } else if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            int index = 0;
            while (index < arrayNode.size()) {
                JsonNode node = arrayNode.get(index);
                ImmutablePair<String, List<String>> result = matcherValue(node, false, false, configmap);
                if (result == null) {
                    arrayNode.set(index, replaceCryPlaceholder(node, configmap));
                } else {
                    String value = result.getKey();
                    arrayNode.set(index, value == null ? NullNode.getInstance() : TextNode.valueOf(value));
                }
                index++;
            }
        }
        return jsonNode;
    }

    public static MetricsInfo replaceSmilingPlaceholder(MetricsInfo metricsInfo, Map<String, Configmap> configmap){
        JsonNode node = JsonUtil.toNode(metricsInfo);
        replaceSmilingPlaceholder(node, configmap);
        return JsonUtil.nodeBean(node, metricsInfo.getClass());
    }


    public static JsonNode replaceSmilingPlaceholder(JsonNode jsonNode, Map<String, Configmap> configmap) {
        if (jsonNode.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> nodes = jsonNode.fields();
            while (nodes.hasNext()) {
                Map.Entry<String, JsonNode> mapNode = nodes.next();
                JsonNode node = mapNode.getValue();
                String key = mapNode.getKey();
                ObjectNode on = (ObjectNode) jsonNode;
                // Replace the attributes of the KEY-VALUE case such as http headers params
                if (key != null && key.startsWith(SMILING_PLACEHOLDER) && key.endsWith(SMILING_PLACEHOLDER)) {
                    key = key.replaceAll(SMILING_PLACEHOLDER_REX, StringUtils.EMPTY);
                    Configmap param = configmap.get(key);
                    if (param != null && param.getType() == AlerterContext.PARAM_TYPE_MAP) {
                        String jsonValue = (String) param.getValue();
                        Map<String, String> map = JsonUtil.toMap(jsonValue, String.class);
                        if (map != null) {
                            map.forEach((name, value) -> {
                                if (StringUtils.isNotBlank(name)) {
                                    on.put(name, value);
                                }
                            });
                        }
                    }
                    nodes.remove();
                    continue;
                }
                // 基础数据类型
                ImmutablePair<String, List<String>> result = matcherValue(node, false,true, configmap);
                if (result == null) {
                    on.set(mapNode.getKey(), replaceSmilingPlaceholder(mapNode.getValue(), configmap));
                } else {
                    String value = result.getKey();
                    if (StringUtils.isNotBlank(value)) {
                        on.put(mapNode.getKey(), value);
                    }
                }
            }
        } else if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            int index = 0;
            while (index < arrayNode.size()) {
                JsonNode node = arrayNode.get(index);
                // 基础数据类型
                ImmutablePair<String, List<String>> result = matcherValue(node, true, true, configmap);
                if (result == null) {
                    arrayNode.set(index, replaceSmilingPlaceholder(node, configmap));
                } else {
                    String value = result.getKey();
                    List<String> arrayValues = result.getValue();
                    if (CollectionUtils.isNotEmpty(arrayValues)) {
                        arrayNode.remove(index);
                        index--;
                        for (String arrayValue : arrayValues) {
                            arrayNode.add(arrayValue);
                            index++;
                        }
                    } else {
                        arrayNode.set(index, value == null ? NullNode.getInstance() : TextNode.valueOf(value));
                    }
                }
                index++;
            }
        }
        return jsonNode;
    }

    /**
     * 匹配替换 不是基础类型返回 null
     * 未匹配到 全部返回空
     * 匹配到的返回
     * @param node 节点
     * @param isArray 是否数组
     * @param configmap map
     * @return 结果
     */
    private static ImmutablePair<String, List<String>> matcherValue (JsonNode node, boolean isArray,
                                                                   boolean smiling, Map<String, Configmap> configmap){
        if (isJsonPrimitive(node)) {
            String value = node.toString();
            Pattern pattern;
            String regex;
            if (smiling) {
                pattern = SMILING_PLACEHOLDER_REGEX_PATTERN;
                regex = SMILING_PLACEHOLDER_REX;
            } else {
                pattern = CRYING_PLACEHOLDER_REGEX_PATTERN;
                regex = CRYING_PLACEHOLDER_REX;
            }
            Matcher smilingMatcher = pattern.matcher(value);
            if (smilingMatcher.find()) {
                smilingMatcher.reset();
                List<String> arrayValues = null;
                while (smilingMatcher.find()) {
                    String group = smilingMatcher.group();
                    String replaceField = group.replaceAll(regex, StringUtils.EMPTY);
                    Configmap param = configmap.get(replaceField);
                    if (param != null) {
                        if (param.getValue() == null) {
                            if (group.length() == value.length()) {
                                value = null;
                                break;
                            } else {
                                value = value.replace(group, StringUtils.EMPTY);
                            }
                        } else if (isArray && param.getType() == AlerterContext.PARAM_TYPE_ARRAY) {
                            arrayValues = PojoUtil.singleToList(String.valueOf(param.getValue()));
                        } else {
                            value = value.replace(group, (String) param.getValue());
                        }
                    } else if (isArray){
                        value = null;
                        break;
                    }
                }
                return ImmutablePair.of(value, arrayValues);
            }

        }
        return null;
    }

    private static boolean isJsonPrimitive(JsonNode node){
        JsonNodeType type = node.getNodeType();
        return type == JsonNodeType.BOOLEAN || type == JsonNodeType.NUMBER || type == JsonNodeType.STRING;
    }


    public static String replaceUriSpecialChar(String uri) {
        uri = uri.replaceAll(StringUtils.SPACE, "%20");
        // todo more special
        return uri;
    }
    

    public static void replaceFieldsForPushStyleMonitor(MetricsInfo metrics, Map<String, Configmap> configmap) {
        Configmap fields = configmap.get("fields");
        if (fields != null) {
            String v = PojoUtil.cast(fields.getValue());
            List<MetricsField> pushFieldList = JsonUtil.toPojos(v, MetricsField.class);
            metrics.setFields(pushFieldList);
        }
    }

    /**
     * convert 16 hexString to byte[]
     * eg: 302c0201010409636f6d6d756e697479a11c020419e502e7020100020100300e300c06082b060102010102000500
     * 16进制字符串不区分大小写，返回的数组相同
     * @param hexString 16 hexString
     * @return byte[]
     */
    public static byte[] fromHexString(String hexString) {
        if (StringUtils.isBlank(hexString)) {
            return null;
        }
        byte[] bytes = new byte[hexString.length() / HEX_STR_WIDTH];
        String hex;
        for (int i = 0; i < hexString.length() / HEX_STR_WIDTH; i++) {
            hex = hexString.substring(i * HEX_STR_WIDTH, i * HEX_STR_WIDTH + HEX_STR_WIDTH);
            bytes[i] = (byte) Integer.parseInt(hex, 16);
        }
        return bytes;
    }
}
