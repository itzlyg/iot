package cn.sinozg.applet.turbo.engine.common;

public class NodeInstanceStatus {
    /** 数据库默认值 */
    public static final int DEFAULT = 0;
    /** 处理成功 */
    public static final int COMPLETED = 1;
    /** 处理中 */
    public static final int ACTIVE = 2;
    /** 处理失败 */
    public static final int FAILED = 3;
    /** 处理已撤销 */
    public static final int DISABLED = 4;
}
