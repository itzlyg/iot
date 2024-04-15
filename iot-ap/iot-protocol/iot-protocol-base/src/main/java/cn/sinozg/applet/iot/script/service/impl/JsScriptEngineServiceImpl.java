package cn.sinozg.applet.iot.script.service.impl;

import cn.sinozg.applet.common.constant.BaseConstants;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.common.utils.StringUtil;
import cn.sinozg.applet.iot.common.constant.ProtocolContext;
import cn.sinozg.applet.iot.common.enums.ScriptEnvType;
import cn.sinozg.applet.iot.script.service.ScriptEngineService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;

/**
 * js 脚本解析器
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-15 17:11
 */
@Slf4j
public class JsScriptEngineServiceImpl implements ScriptEngineService {
    private final Context context = Context.newBuilder(LANGUAGE_ID).allowHostAccess(HostAccess.ALL).build();

    private Value js;

    private static final String LANGUAGE_ID = "js";
    @Override
    public void setScript(String script) {
        String tempScript = "new (function () {\n" +
                "    %s\n" +
                "    this.invoke = function (f) {\n" +
                "        let len = arguments.length\n" +
                "        let  args = []\n" +
                "        for (let i = 1; i < len; i++) {\n" +
                "            try {\n" +
                "                let temp = JSON.parse(arguments[i]);\n" +
                "                args[i - 1] = temp\n" +
                "            } catch (e) {\n" +
                "                args[i - 1] = arguments[i]\n" +
                "            }\n" +
                "        }\n" +
                "        return JSON.stringify(this[f].apply(this, args));\n" +
                "    }\n" +
                "})()";
        CharSequence text =  String.format(tempScript, script);
        js = context.eval(LANGUAGE_ID, text);
    }

    @Override
    public void putScriptEnv(ScriptEnvType key, Object val) {
        context.getBindings(LANGUAGE_ID).putMember(key.getCode(), val);
    }

    @Override
    public void invokeMethod(String methodName, Object... args) {
        invokeMethod(null, methodName, args);
    }

    @Override
    public <T> T invokeMethod(Class<T> resultType, String methodName, Object... args) {
        Value member = js.getMember("invoke");
        StringBuilder sbArgs = formatArgs(args);
        try {
            int len = args.length;
            Value rst;
            // 通过调用invoke方法将目标方法返回结果转成json
            if (len == 1) {
                rst = member.execute(methodName, args[0]);
            } else if (len == 2) {
                rst = member.execute(methodName, args[0], args[1]);
            } else if (len == 3) {
                rst = member.execute(methodName, args[0], args[1], args[2]);
            } else {
                rst = member.execute(methodName, args);
            }
            String json = rst.asString();
            // 没有返回值
            if (json == null || resultType == null || StringUtils.equalsAny(json, ProtocolContext.NULL, ProtocolContext.PONG)) {
                return null;
            }
            return JsonUtil.toPojo(json, resultType);
        } catch (Exception e) {
            log.error("脚本解析数据错误！！方法：{} ，参数1：{}，参数2：{}，参数3：{}", methodName, args[0], PojoUtil.cast(args[1]), PojoUtil.cast(args[2]));
            throw new CavException("脚本解析数据错误！", e);
        }

    }

    private static StringBuilder formatArgs(Object[] args) {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null && !(args[i] instanceof String)) {
                args[i] = JsonUtil.toJson(args[i]);
            }
            result.append(args[i]).append(BaseConstants.COMMA);
        }
        StringUtil.delLast(result);
        result.append("]");
        return result;
    }
}
