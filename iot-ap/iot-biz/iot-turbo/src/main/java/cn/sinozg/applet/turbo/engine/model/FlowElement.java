package cn.sinozg.applet.turbo.engine.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class FlowElement {
    /** 流程内元素唯一key resourceId */
    private String key;
    /** stencil 类型 */
    private int type;
    private List<String> outgoing;
    /** 配置属性 */
    private Map<String, Object> properties;
    private List<String> incoming;
}
