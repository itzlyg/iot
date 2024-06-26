package cn.sinozg.applet.turbo.engine.util.impl;

import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.turbo.engine.common.ErrorEnum;
import cn.sinozg.applet.turbo.engine.exception.ProcessException;
import cn.sinozg.applet.turbo.engine.util.ExpressionCalculator;
import cn.sinozg.applet.turbo.engine.util.GroovyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Map;

@Service
public class GroovyExpressionCalculator implements ExpressionCalculator {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroovyExpressionCalculator.class);

    @Override
    public Boolean calculate(String expression, Map<String, Object> dataMap) throws ProcessException {
        if (expression.startsWith("${") && expression.endsWith("}")) {
            expression = expression.substring(2, expression.length() - 1);
        }
        Object result = null;
        try {
            result = GroovyUtil.execute(expression, dataMap);
            if (result instanceof Boolean) {
                return (Boolean) result;
            } else {
                LOGGER.warn("the result of expression is not boolean.||expression={}||result={}||dataMap={}",
                        expression, result, JsonUtil.toJson(dataMap));
                throw new ProcessException(ErrorEnum.GROOVY_CALCULATE_FAILED.getErrNo(), "expression is not instanceof bool.");
            }
        } catch (Exception e) {
            LOGGER.error("calculate expression failed.||message={}||expression={}||dataMap={}, ", e.getMessage(), expression, dataMap, e);
            String groovyExFormat = "{0}: expression={1}";
            throw new ProcessException(ErrorEnum.GROOVY_CALCULATE_FAILED, MessageFormat.format(groovyExFormat, e.getMessage(), expression));
        } finally {
            LOGGER.info("calculate expression.||expression={}||dataMap={}||result={}", expression, JsonUtil.toJson(dataMap), result);
        }
    }
}
