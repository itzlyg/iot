package cn.sinozg.applet.iot.script.factory;

import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.iot.common.enums.ScriptType;
import cn.sinozg.applet.iot.script.service.ScriptEngineService;
import cn.sinozg.applet.iot.script.service.DataAnalysisService;
import org.apache.commons.lang3.StringUtils;

/**
 * 获取到脚本实现的实例
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-16 15:26
 */
public class ScriptFactory {

    /**
     * 获取到脚本引擎对象
     * @param code code
     * @return 引擎对象
     */
    public static ScriptEngineService scriptEngine(String code) {
        ScriptType type = scriptType(code, null);
        return scriptEngine(type);
    }

    /**
     * 获取到脚本引擎对象
     * @param type 类型
     * @return 引擎对象
     */
    public static ScriptEngineService scriptEngine(ScriptType type) {
        type = scriptType(null, type);
        return scriptInfo(type.getEngineClazz());
    }

    /**
     * 获取到转化器的实现
     * @param code code
     * @return 实现
     */
    public static DataAnalysisService scriptConverter(String code){
        ScriptType type = scriptType(code, null);
        return scriptInfo(type.getConvertClazz());
    }

    /**
     * 获取到对象
     * @param clazz 类型
     * @return 对象
     * @param <T> 类型
     */
    private static <T> T scriptInfo (Class<T> clazz){
        try {
            return PojoUtil.newInstance(clazz);
        } catch (Exception e) {
            throw new CavException("创建脚本引擎失败！", e);
        }
    }

    /**
     * 获取到对应的枚举值
     * @param code code
     * @param type 枚举值
     * @return 枚举值
     */
    private static ScriptType scriptType (String code, ScriptType type){
        if (StringUtils.isNotEmpty(code)) {
            type = ScriptType.ofCode(code);
        }
        if (type == null) {
            type = ScriptType.JS;
        }
        return type;
    }
}
