package cn.sinozg.applet.biz.sink.util;

import cn.sinozg.applet.biz.com.model.TmBaseDataTypeInfo;
import cn.sinozg.applet.biz.com.model.TmDataTypeInfo;
import cn.sinozg.applet.biz.com.vo.request.TmSinkRequest;
import cn.sinozg.applet.biz.sink.common.enums.DbDataType;
import cn.sinozg.applet.biz.sink.model.TdField;
import cn.sinozg.applet.common.utils.PojoUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-06 14:29
 */
public class FieldUtil {

    private FieldUtil() {
    }

    public static List<TdField> parseToField(TmSinkRequest info) {
        return PojoUtil.toList(info.getAttributes(), FieldUtil::parseProperty);
    }

    private static TdField parseProperty(TmBaseDataTypeInfo attributes) {
        String filedName = attributes.getIdentifier().toLowerCase();
        TmDataTypeInfo dataType = attributes.getDataType();
        // 将物模型字段类型映射为td字段类型
        DbDataType fType = DbDataType.ofCode(dataType.getType());
        // max and min
        Object specs = dataType.getSpecs();
        int len = -1;
        if (specs instanceof Map) {
            Object objLen = ((Map<?, ?>) specs).get("length");
            if (objLen != null) {
                len = Integer.parseInt(objLen.toString());
            }
        }
        return new TdField(filedName, fType, len);
    }

    /**
     * 将从库中查出来的字段信息转换为td字段对象
     * 数据结构如下：[["time","TIMESTAMP",8,""],["powerstate","TINYINT",1,""],["brightness","INT",4,""],["deviceCode","NCHAR",32,"TAG"]]
     * @param rows 返回的数据
     * @return 字段
     */
    public static Map<String, TdField> parseList(List<List<Object>> rows) {
        Map<String, TdField> map = new HashMap<>(64);
        for (List<Object> row : rows) {
            String name = row.get(0).toString();
            String type = row.get(1).toString().toUpperCase();
            int len = -1;
            if (DbDataType.STRING.getTdType().equals(type)) {
                len = Integer.parseInt(row.get(2).toString());
            }
            map.put(name, new TdField(name, DbDataType.ofType(type), len));
        }
        return map;
    }
}
