package cn.sinozg.applet.turbo.engine.common;

public class FlowElementType {
    public static final int SEQUENCE_FLOW = 1;
    public static final int START_EVENT = 2;
    public static final int END_EVENT = 3;
    public static final int USER_TASK = 4;
    public static final int EXCLUSIVE_GATEWAY = 6;
    public static final int CALL_ACTIVITY = 8;

    public static final String NM_SEQUENCE_FLOW = "sequenceFlowExecutor";
    public static final String NM_START_EVENT = "startEventExecutor";
    public static final String NM_END_EVENT = "endEventExecutor";
    public static final String NM_USER_TASK = "userTaskExecutor";
    public static final String NM_EXCLUSIVE_GATEWAY = "exclusiveGatewayExecutor";
    public static final String NM_CALL_ACTIVITY = "syncSingleCallActivityExecutor";
}
