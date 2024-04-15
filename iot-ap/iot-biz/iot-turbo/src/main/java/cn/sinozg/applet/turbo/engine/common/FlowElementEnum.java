package cn.sinozg.applet.turbo.engine.common;

import lombok.Getter;

@Getter
public enum FlowElementEnum {
    /** SEQUENCE_FLOW */
    SEQUENCE_FLOW(FlowElementType.SEQUENCE_FLOW, FlowElementType.NM_SEQUENCE_FLOW),
    START_EVENT(FlowElementType.START_EVENT, FlowElementType.NM_START_EVENT),
    END_EVENT(FlowElementType.END_EVENT, FlowElementType.NM_END_EVENT),
    USER_TASK(FlowElementType.USER_TASK, FlowElementType.NM_USER_TASK),
    EXCLUSIVE_GATEWAY(FlowElementType.EXCLUSIVE_GATEWAY, FlowElementType.NM_EXCLUSIVE_GATEWAY),
    CALL_ACTIVITY(FlowElementType.CALL_ACTIVITY, FlowElementType.NM_CALL_ACTIVITY),
    ;

    private final int code;

    private final String beanName;

    FlowElementEnum(int code, String beanName){
        this.code = code;
        this.beanName = beanName;
    }

    public static FlowElementEnum ofName (String beanName){
        for (FlowElementEnum value : values()) {
            if (value.getBeanName().equals(beanName)) {
                return value;
            }
        }
        return null;
    }
}
