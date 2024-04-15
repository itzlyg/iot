package cn.sinozg.applet.iot.script.service.impl;

import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.common.utils.SpringUtil;
import cn.sinozg.applet.iot.common.constant.ProtocolContext;
import cn.sinozg.applet.iot.common.enums.ScriptEnvType;
import cn.sinozg.applet.iot.script.service.ScriptEngineService;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * java 脚本解析器 java包
 * 指定 主类 对应的方法
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-15 17:11
 */
@Slf4j
public class JavaScriptEngineServiceImpl implements ScriptEngineService {

    private final Object bean = SpringUtil.getBean(ProtocolContext.JAVA_ANALYSIS_BEAN);
    @Override
    public void setScript(String script) {
    }

    @Override
    public void putScriptEnv(ScriptEnvType key, Object val) {

    }

    @Override
    public void invokeMethod(String methodName, Object... args) {
        invokeMethod(null, methodName, args);
    }

    @Override
    public <T> T invokeMethod(Class<T> resultType, String methodName, Object... args) {
        if (bean == null) {
            log.error("未实现java 解析编码 类 {}", ProtocolContext.JAVA_ANALYSIS_BEAN);
            return null;
        }
        Class<?>[] parameterTypes = null;
        if (args != null) {
            parameterTypes = new Class[args.length];
            for (int i = 0, j = args.length; i < j; i++) {
                if (args[i] instanceof HashMap) {
                    parameterTypes[i] = Map.class;
                } else {
                    parameterTypes[i] = args[i].getClass();
                }
            }
        }
        try {
            Method method = bean.getClass().getMethod(methodName, parameterTypes);
            Object result = method.invoke(bean, args);
            if (result == null || resultType == null) {
                return null;
            }
            if (result.getClass() == resultType) {
                return PojoUtil.cast(result);
            } else if (result instanceof String) {
                return JsonUtil.toPojo(PojoUtil.cast(result), resultType);
            }
        } catch (Exception e) {
            log.error("反射方法失败！", e);
        }

        return null;
    }
}
