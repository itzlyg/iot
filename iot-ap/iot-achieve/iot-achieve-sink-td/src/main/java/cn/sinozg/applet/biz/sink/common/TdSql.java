package cn.sinozg.applet.biz.sink.common;

/**
 * sql 模版
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-07 15:53
 */
public class TdSql {
    /** 创建超级表模板（含存在判断） */
    public static final String STB_CREATE = "CREATE STABLE IF NOT EXISTS %s (%s) TAGS (%s);";

    /** 删除超级表 */
    public static final String STB_DROP = "DROP STABLE IF EXISTS %s;";

    /** 获取表的结构信息 */
    public static final String DESC_TB_TPL = "DESCRIBE %s;";
    /** 创建数据库 */
    public static final String DB_CREATE = "CREATE DATABASE IF NOT EXISTS iot KEEP 365 DURATION 10 BUFFER 16 WAL_LEVEL 1;";

    /** 超级表增加列 */
    public static final String STB_ADD_COL = "ALTER STABLE %s ADD COLUMN %s;";

    /** 超级表修改列 */
    public static final String STB_MODIFY_COL = "ALTER STABLE %s MODIFY COLUMN %s;";

    /** 超级表删除列  */
    public static final String STB_DROP_COL = "ALTER STABLE %s DROP COLUMN %s;";


    /** 属性历史记录  */
    public static final String PRO_HIS = "select ts, %s as `value`,device_code from %s where device_code=? and ts>=? and ts<=? order by ts asc limit 0 , %d";

    /** 最新的一条数据  */
    public static final String PRO_LAST = "select * from %s where device_code = ? order by ts desc limit 1 ";

    /** 属性新增  */
    public static final String PRO_ADD = "INSERT INTO %s (ts, %s) USING %s TAGS ('%s') VALUES (?,%s);";


    /** 任务记录删除  */
    public static final String TASK_DEL = "delete from t_task_info_log where task_id=? and ts<=NOW()";

    /** 任务记录 新增  */
    public static final String TASK_ADD = "INSERT INTO t_task_info_log_%s (ts,content,success) USING t_task_info_log TAGS ('%s') VALUES (?,?,?);";

    /** 任务记录 分页总数  */
    public static final String TASK_PAGE_COUNT = "select count(*) from t_task_info_log where task_id=?";

    /** 任务记录 分页  */
    public static final String TASK_PAGE_DETAIL = "select ts,content,success,task_id from t_task_info_log where task_id=? order by ts desc limit %d offset %d";


    /** 物模型 新增  */
    public static final String TH_ADD = "INSERT INTO t_thing_model_message_%s (ts,mid, mid_name,prod_key,device_name,  uid,type,identifier,code,data,   report_time) " +
            "USING t_thing_model_message TAGS ('%s') VALUES (?,?,?,?,?,  ?,?,?,?,?   ,?);";

    /** 物模型 分页总数  */
    public static final String TH_PAGE_COUNT = "select count(*) from t_thing_model_message where device_code=? %s";

    /** 物模型 分页  */
    public static final String TH_PAGE_DETAIL = "select ts, mid_name, mid,prod_key,device_name,type,identifier,code,data,report_time " +
            "from t_thing_model_message where device_code=? %s order by ts desc limit %d offset %d";

    /** 物模型 统计  */
    public static final String TH_STA = "select ts,count(*) as data from(select ts TIMETRUNCATE(ts,1h) as ts from t_thing_model_message " +
            "where ts>=? and ts<=? %s ) a group by ts order by ts asc";

    /** 物模型 总计   */
    public static final String TH_COUNT = "select count(*) from t_thing_model_message ";



    /****** table *****/
    public static final String TB_RULE = "t_rule_info_log";

    public static final String TB_TASK = "t_task_info_log";

    public static final String TB_TH = "t_thing_model_message";

    public static final String TB_VIRTUAL = "t_virtual_device_log";

}
