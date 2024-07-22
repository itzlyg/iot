package cn.sinozg.applet.biz.sink.service.impl;

import cn.sinozg.applet.biz.com.vo.request.TmSinkRequest;
import cn.sinozg.applet.biz.sink.common.TdContext;
import cn.sinozg.applet.biz.sink.common.TdSql;
import cn.sinozg.applet.biz.sink.common.enums.DbDataType;
import cn.sinozg.applet.biz.sink.model.TdField;
import cn.sinozg.applet.biz.sink.model.response.TdRestResponse;
import cn.sinozg.applet.biz.sink.service.DbStructureService;
import cn.sinozg.applet.biz.sink.util.FieldUtil;
import cn.sinozg.applet.biz.sink.util.RestUtil;
import cn.sinozg.applet.biz.sink.util.TdTableUtil;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.PojoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-06 13:38
 */
@Slf4j
@Service
public class DbStructureServiceImpl implements DbStructureService {
    private final TdField timeField = TdField.other(TdContext.TIMESTAMP, DbDataType.TIMESTAMP);
    @Override
    public void syncThingModel(TmSinkRequest info, boolean create) {
        if (CollectionUtils.isEmpty(info.getAttributes())) {
            throw new CavException("请定义产品属性！");
        }
        if (create) {
            initThingModel(info);
        } else {
            updateThingModel(info);
        }
    }

    @Override
    public void initDbStructure(boolean delete) {
        RestUtil.executeSql(TdSql.DB_CREATE, "初始化数据库失败！");
        if (delete) {
            RestUtil.executeSql(String.format(TdSql.STB_DROP, TdSql.TB_RULE), "删除规则日志超级表失败!");
            RestUtil.executeSql(String.format(TdSql.STB_DROP, TdSql.TB_TASK), "删除任务日志超级表失败!");
            RestUtil.executeSql(String.format(TdSql.STB_DROP, TdSql.TB_TH), "删除模型消息超级表失败!");
            RestUtil.executeSql(String.format(TdSql.STB_DROP, TdSql.TB_VIRTUAL), "删除虚拟设备日志表失败!");
        }
        // 创建规则日志超级表
        String sql = TdTableUtil.createSuperTableSql(TdSql.TB_RULE,
                PojoUtil.arrayToList(timeField,
                TdField.string("state_val", 32),
                TdField.string("content", 4096),
                TdField.other("success", DbDataType.BOOLEAN)),
                TdField.string("rule_id"));
        RestUtil.executeSql(sql, "创建规则日志超级表失败！");

        // 创建任务日志
        sql = TdTableUtil.createSuperTableSql(TdSql.TB_TASK,
                PojoUtil.arrayToList(timeField,
                TdField.string("content", 4096),
                TdField.other("success", DbDataType.BOOLEAN)),
                TdField.string("task_id"));
        RestUtil.executeSql(sql, "创建任务日志超级表失败！");

        // 创建物模型消息超级表
        sql = TdTableUtil.createSuperTableSql(TdSql.TB_TH,
                PojoUtil.arrayToList(timeField,
                TdField.string("mid"),
                TdField.string("mid_name", 255),
                TdField.string("prod_key"),
                TdField.string("device_name", 255),
                TdField.string("uid"),
                TdField.string("type"),
                TdField.string("identifier"),
                TdField.string("order_tp"),
                TdField.other("code", DbDataType.INT),
                TdField.string("data", 4096),
                TdField.other("report_time", DbDataType.LONG)),
                TdField.string("device_code"));
        RestUtil.executeSql(sql, "创建物模型消息超级表失败！");

        // 创建虚拟设备日志超级表
        sql = TdTableUtil.createSuperTableSql(TdSql.TB_VIRTUAL,
                PojoUtil.arrayToList(timeField,
                TdField.string("virtual_device_name", 255),
                TdField.other("device_total", DbDataType.INT),
                TdField.string("result", 4096)),
                TdField.string("virtual_device_code"));
        RestUtil.executeSql(sql, "创建虚拟设备日志超级表失败！");
    }


    private void initThingModel(TmSinkRequest info) {
        // 获取物模型中的属性定义
        List<TdField> fields = new ArrayList<>();
        fields.add(timeField);
        fields.addAll(FieldUtil.parseToField(info));
        String tableName = TdTableUtil.prodPropertySuperTableName(info.getProdKey());
        // 生成sql
        String sql = TdTableUtil.createSuperTableSql(tableName, fields, TdField.string(TdContext.DEVICE_CODE));
        if (sql == null) {
            return;
        }
        RestUtil.executeSql(sql, "新增表结构失败！");
    }

    /**
     * 根据物模型更新超级表结构
     */
    private void updateThingModel(TmSinkRequest info) {
        // 获取旧字段信息
        String tableName = TdTableUtil.prodPropertySuperTableName(info.getProdKey());
        String sql = TdTableUtil.descTableSql(tableName);
        TdRestResponse response = RestUtil.executeSql(sql, "获取属性表结构错误！");
        List<TdField> newFields = FieldUtil.parseToField(info);

        // 对比差异
        Map<String, TdField> oldMap = FieldUtil.parseList(response.getData());
        Map<String, TdField> newMap = PojoUtil.toMap(newFields, TdField::getName);
        List<TdField> addList = new ArrayList<>();
        List<TdField> updateList = new ArrayList<>();
        List<TdField> deleteList = new ArrayList<>();
        for (Map.Entry<String, TdField> e : newMap.entrySet()) {
            // 新增
            if (!oldMap.containsKey(e.getKey())) {
                addList.add(e.getValue());
            } else {
                TdField temp = oldMap.get(e.getKey());
                // 修改
                if (!e.getValue().getType().getTdType().equals(temp.getType().getTdType()) ||
                        e.getValue().getLength() != temp.getLength()) {
                    updateList.add(temp);
                }
            }
        }
        for (Map.Entry<String, TdField> e : oldMap.entrySet()) {
            if (!StringUtils.equalsAny(e.getKey(),TdContext.DEVICE_CODE, TdContext.TIMESTAMP) && !newMap.containsKey(e.getKey())) {
                deleteList.add(e.getValue());
            }
        }

        if (CollectionUtils.isNotEmpty(addList)) {
            sql = TdTableUtil.addSuperTableColumnSql(tableName, addList);
            RestUtil.executeSql(sql, "新增属性表字段错误！");
        }

        if (CollectionUtils.isNotEmpty(updateList)) {
            sql = TdTableUtil.updateSuperTableColumnSql(tableName, updateList);
            RestUtil.executeSql(sql, "修改属性表字段错误！");
        }

        if (CollectionUtils.isNotEmpty(deleteList)) {
            sql = TdTableUtil.dropSuperTableColumnSql(tableName, deleteList);
            RestUtil.executeSql(sql, "删除属性表字段错误！");
        }
    }
}
