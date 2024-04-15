package cn.sinozg.applet.turbo.engine.common;

public class FlowInstanceStatus {
    /** 数据库默认值 */
    public static final int DEFAULT = 0;
    /** 执行完成 */
    public static final int COMPLETED = 1;
    /** 执行中 */
    public static final int RUNNING = 2;
    /** 已终止 */
    public static final int TERMINATED = 3;
    /** 新增子流程实例的执行结束的终态，主要是解决从父流程实例回滚到已执行结束的子流程实例的情况  执行结束*/
    public static final int END = 4;
}
