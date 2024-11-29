package cn.sinozg.applet.biz.notify.dispatch.impl;

import cn.sinozg.applet.biz.notify.enums.ConditionType;
import cn.sinozg.applet.biz.notify.execute.AlerterFactory;
import cn.sinozg.applet.biz.notify.model.NoticeConditionInfo;
import cn.sinozg.applet.biz.notify.model.NoticeDefineInfo;
import cn.sinozg.applet.biz.notify.model.send.SendNoticeRule;
import cn.sinozg.applet.biz.notify.service.AlerterDataService;
import cn.sinozg.applet.common.constant.Constants;
import cn.sinozg.applet.common.exception.CavException;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.exception.CompileExpressionErrorException;
import com.googlecode.aviator.exception.ExpressionRuntimeException;
import com.googlecode.aviator.exception.ExpressionSyntaxErrorException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-29 17:10
 */
@Slf4j
@Component
public class FilterDispatcher {

    private static final Map<String, List<NoticeDefineInfo>> DEFINE_MAP = new ConcurrentHashMap<>();

    @Resource
    private AlerterDataService alerterDataService;

    /**
     * 匹配配置
     * @param prodId 产品id
     * @param deviceId 设备id
     * @param deviceData 设备数据
     */
    public void matcherDefine (String prodId, String deviceId, Map<String, Object> deviceData){
        List<NoticeDefineInfo> defineList = DEFINE_MAP.get(prodId);
        List<NoticeDefineInfo> matchDevice = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(defineList)) {
            for (NoticeDefineInfo d : defineList) {
                if (CollectionUtils.isEmpty(d.getDeviceIds()) || d.getDeviceIds().contains(deviceId)) {
                    boolean match ;
                    if (StringUtils.isNotBlank(d.getExpr())) {
                        match = execExpression(deviceData, d.getExpr());
                    } else {
                        match = matchCondition(d.getConditions(), deviceData);
                    }
                    if (match) {
                        matchDevice.add(d);
                    }
                }
            }
        }
        // 匹配到的规则 发送
        if (CollectionUtils.isNotEmpty(matchDevice)) {
            for (NoticeDefineInfo info : matchDevice) {
                // DefineId 找到规则id 以及对应的 模板id 渠道id等
                Map<String, SendNoticeRule> noticeRuleMap = alerterDataService.noticeRuleByDefId(info);
                if (MapUtils.isNotEmpty(noticeRuleMap)) {
                    for (Map.Entry<String, SendNoticeRule> e : noticeRuleMap.entrySet()) {
                        SendNoticeRule r = e.getValue();
                        AlerterFactory.sendNoticeMsg(r.getConfig(), r.getTemplate(), r.getAlerter());
                    }
                }

            }
        }

    }

    /**
     * 表达式匹配
     * @param fieldValueMap 值
     * @param expr 表达式
     * @return 值
     */
    private boolean execExpression(Map<String, Object> fieldValueMap, String expr) {
        Boolean match;
        try {
            Expression expression = AviatorEvaluator.compile(expr, true);
            expression.getVariableNames().forEach(variable -> {
                if (!fieldValueMap.containsKey(variable)) {
                    throw new ExpressionRuntimeException("metrics value not contains expr field: " + variable);
                }
            });
            match = (Boolean) expression.execute(fieldValueMap);
        } catch (CompileExpressionErrorException | ExpressionSyntaxErrorException compileException) {
            log.error("Alert Define Rule: {} Compile Error: {}.", expr, compileException.getMessage());
            throw compileException;
        } catch (ExpressionRuntimeException expressionRuntimeException) {
            log.error("Alert Define Rule: {} Run Error: {}.", expr, expressionRuntimeException.getMessage());
            throw expressionRuntimeException;
        } catch (Exception e) {
            log.error("Alert Define Rule: {} Unknown Error: {}.", expr, e.getMessage());
            throw e;
        }
        return match != null && match;

    }

    /**
     * 规则条件匹配
     * @param conditions 条件
     * @param deviceData 设备数据信息
     * @return 是否匹配
     */
    private boolean matchCondition(List<NoticeConditionInfo> conditions, Map<String, Object> deviceData){
        // 没有规则返回 true
        if (CollectionUtils.isEmpty(conditions)) {
            return true;
        }
        // 只考虑 and 单层
        String field;
        for (NoticeConditionInfo condition : conditions) {
            field = condition.getField();
            Object o = deviceData.get(field);
            // 没有值，则没有匹配到
            if (!operation(condition.getFieldType(), condition.getCondition(), o, condition.getCompValue())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 运算符号
     * @param type 类型
     * @param condition 条件
     * @param o 原始值
     * @param n 对比值
     * @return 是否匹配
     */
    private boolean operation(String type, String condition, Object o, String n){
        if (o == null) {
            return false;
        }
        String ov = String.valueOf(o);
        // 数字
        ConditionType conditionType = ConditionType.ofCode(condition);
        Boolean result;
        switch (type) {
            case Constants.STATUS_01 :
                result = operationNum(ov, n, conditionType);
                break;
            case Constants.STATUS_03 :
                result = operationBoolean(ov, n, conditionType);
                break;
            default:
                result = operationStr(ov, n, conditionType);
                break;
        }
        if (result == null){
            throw new CavException("数据类型和匹配规则不匹配！");
        }
        return result;
    }

    /**
     * 数字 类型的运算
     * @param o 原始值
     * @param n 对比值
     * @param type 类型
     * @return 是否匹配
     */
    private Boolean operationNum(String o, String n, ConditionType type) {
        BigDecimal on = new BigDecimal(o);
        int compare = on.compareTo(new BigDecimal(n));
        switch (type) {
            case GT: return compare > 0;
            case LT: return compare < 0;
            case EQ: return compare == 0;
            case GE: return compare > -1;
            case LE: return compare < 1;
            case NE: return compare != 0;
            default: return null;
        }
    }

    /**
     * 布尔 类型的运算
     * @param o 原始值
     * @param n 对比值
     * @param type 类型
     * @return 是否匹配
     */
    private Boolean operationBoolean(String o, String n, ConditionType type) {
        Boolean ob = Boolean.parseBoolean(o);
        Boolean nb = Boolean.parseBoolean(n);
        switch (type) {
            case EQ: return ob.equals(nb);
            case NE: return !ob.equals(nb);
            default: return null;
        }
    }

    /**
     * string 类型的运算
     * @param o 原始值
     * @param n 对比值
     * @param type 类型
     * @return 是否匹配
     */
    private Boolean operationStr(String o, String n, ConditionType type) {
        switch (type) {
            case EQ: return o.equals(n);
            case NE: return !o.equals(n);
            case MATCHES: return o.matches(n);
            case MATCHES_NON: return !o.matches(n);
            case CONTAINS: return o.contains(n);
            case CONTAINS_NON: return !o.contains(n);
            default: return null;
        }
    }
}
