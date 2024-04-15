package cn.sinozg.applet.iot.protocol.cache;

import cn.sinozg.applet.common.constant.BaseConstants;
import cn.sinozg.applet.iot.common.utils.ProtocolUtil;
import cn.sinozg.applet.iot.script.factory.ScriptFactory;
import cn.sinozg.applet.iot.script.service.DataAnalysisService;
import cn.sinozg.applet.iot.script.service.ScriptEngineService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-05 14:22
 */
@Slf4j
public class ScriptCache {
    private static final Map<String, DataAnalysisService> ANALY_MAP = new ConcurrentHashMap<>();

    private static final Map<String, ScriptEngineService> PROTOCOL_MAP = new ConcurrentHashMap<>();

    public static String setAnalysis(String type, String id, DataAnalysisService service){
        String key = type + BaseConstants.UNDERLINE + id;
        ANALY_MAP.putIfAbsent(key, service);
        return key;
    }

    public static String setProtocol(String type, String id){
        String key = type + BaseConstants.UNDERLINE + id;
        if (!PROTOCOL_MAP.containsKey(key)) {
            ScriptEngineService protocolScript = ScriptFactory.scriptEngine(type);
            String script = ProtocolUtil.readScript(id, false);
            if (StringUtils.isBlank(script)) {
                log.error("协议脚本信息为空！{},{}", id, type);
            }
            protocolScript.setScript(script);
            PROTOCOL_MAP.put(key, protocolScript);
        }
        return key;
    }

    public static DataAnalysisService getAnalysis (String key){
        return ANALY_MAP.get(key);
    }

    public static ScriptEngineService getProtocol (String key){
        return PROTOCOL_MAP.get(key);
    }
}
