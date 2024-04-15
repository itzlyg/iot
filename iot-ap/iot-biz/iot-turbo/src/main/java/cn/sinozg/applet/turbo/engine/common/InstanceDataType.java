package cn.sinozg.applet.turbo.engine.common;

public class InstanceDataType {
    /** 数据库默认值 */
    public static final int DEFAULT = 0;
    /** 实例初始化 */
    public static final int INIT = 1;
    /** 系统执行 */
    public static final int EXECUTE = 2;
    /** 系统主动获取 */
    public static final int HOOK = 3;
    /** 上游更新 */
    public static final int UPDATE = 4;
    /** 任务提交 */
    public static final int COMMIT = 5;
    /** 任务回滚(暂时无用, 回滚时不产生新数据, 只修改数据版本号(dbId)) */
    public static final int ROLLBACK = 6;
}
