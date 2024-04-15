package cn.sinozg.applet.biz.sink.util;

import cn.sinozg.applet.biz.sink.common.TdSql;
import cn.sinozg.applet.biz.sink.model.TdField;
import cn.sinozg.applet.common.constant.BaseConstants;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.common.utils.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-06 14:35
 */
public class TdTableUtil {

    /**
     * 创建超级表的sql
     * @param tableName 表名称
     * @param fields 字段
     * @param tags tags
     * @return sql
     */
    public static String createSuperTableSql(String tableName, List<TdField> fields, TdField... tags) {
        if (fields.isEmpty()) {
            return null;
        }
        // 生成字段片段
        String fieldSql = appendSql(fields);
        // 生成tag
        String tagSlq = appendSql(PojoUtil.arrayToList(tags));
        return String.format(TdSql.STB_CREATE, tableName, fieldSql, tagSlq);
    }


    /**
     * 根据产品key获取产品属性超级表名
     * @param prodKey prodKey
     * @return 表名称
     */
    public static String prodPropertySuperTableName(String prodKey) {
        return String.format("t_prod_property_%s", prodKey.toLowerCase());
    }


    /**
     * 根据deviceCode获取设备属性表名
     * @param deviceCode deviceCode
     * @return 表名称
     */
    public static String devicePropertyTableName(String deviceCode) {
        return String.format("t_device_property_%s", deviceCode.toLowerCase());
    }

    /**
     * 取正确的表名
     *
     * @param name 表象
     */
    public static String rightTbName(String name) {
        return name.toLowerCase().replace(BaseConstants.MIDDLE_LINE, BaseConstants.UNDERLINE);
    }


    /**
     * 获取表详情的sql
     * @param tableName 表名称
     * @return sql
     */
    public static String descTableSql(String tableName) {
        return String.format(TdSql.DESC_TB_TPL, tableName);
    }

    /**
     * 获取新增字段sql
     * @param tableName tableName
     * @param fields 字段
     * @return sql
     */
    public static String addSuperTableColumnSql(String tableName, List<TdField> fields) {
        return tableColumnSql(tableName, TdSql.STB_ADD_COL, fields);
    }

    /**
     * 获取修改字段sql
     * @param tableName tableName
     * @param fields 字段
     * @return sql
     */
    public static String updateSuperTableColumnSql(String tableName, List<TdField> fields) {
        return tableColumnSql(tableName, TdSql.STB_MODIFY_COL, fields);
    }

    /**
     * 获取删除字段sql
     * @param tableName tableName
     * @param fields 字段
     * @return sql
     */
    public static String dropSuperTableColumnSql(String tableName, List<TdField> fields) {
        return tableColumnSql(tableName, TdSql.STB_DROP_COL, fields, true);
    }

    /**
     * 计数
     * @param counts 记录
     * @return 条数
     */
    public static long count(List<Long> counts) {
        if (CollectionUtils.isNotEmpty(counts) && counts.get(0) != null) {
            return counts.get(0);
        }
        return 0;
    }

    /**
     * 组装 sql 字段
     * @param tableName 表名称
     * @param temp 模版
     * @param fields 字段
     * @return sql
     */
    private static String tableColumnSql (String tableName, String temp, List<TdField> fields){
        return tableColumnSql(tableName, temp, fields, false);
    }
    private static String tableColumnSql (String tableName, String temp, List<TdField> fields, boolean drop){
        StringBuilder sql = new StringBuilder();
        for (TdField field : fields) {
            sql.append(String.format(temp, tableName, columnInfo(null, field, drop)));
        }
        return sql.toString();
    }
    private static String appendSql (List<TdField> fields){
        return appendSql(fields, false);
    }

    private static String appendSql (List<TdField> fields, boolean drop){
        StringBuilder sql = new StringBuilder();
        if (CollectionUtils.isNotEmpty(fields)) {
            for (TdField f : fields) {
                columnInfo(sql, f, drop);
                sql.append(BaseConstants.COMMA);
            }
        }
        StringUtil.delLast(sql);
        return sql.toString();
    }

    /**
     * 获取到字段信息
     * @param sql sql 语句
     * @param f 字段
     * @param drop 删除
     * @return sql
     */
    private static String columnInfo (StringBuilder sql, TdField f, boolean drop){
        if (sql == null) {
            sql = new StringBuilder();
        }
        sql.append(f.getName()).append(StringUtils.SPACE);
        if (!drop) {
            sql.append(f.getType().getTdType());
            if (f.getLength() > 0) {
                sql.append("(").append(f.getLength()).append(")");
            }
        }

        return sql.toString();
    }
}
