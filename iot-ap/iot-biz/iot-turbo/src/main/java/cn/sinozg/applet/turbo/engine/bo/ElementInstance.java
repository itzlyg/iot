package cn.sinozg.applet.turbo.engine.bo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
@Getter
@Setter
public class ElementInstance {

    private String modelKey;
    private String modelName;
    private Map<String, Object> properties;
    private int status;
    private String nodeInstanceId;
    private List<String> subFlowInstanceIdList;
    private List<ElementInstance> subElementInstanceList;
    private String instanceDataId;

    public ElementInstance() {
        super();
    }

    public ElementInstance(String modelKey, int status) {
        this(modelKey, status, null, null);
    }

    public ElementInstance(String modelKey, int status, String nodeInstanceId, String instanceDataId) {
        super();
        this.modelKey = modelKey;
        this.status = status;
        this.nodeInstanceId = nodeInstanceId;
        this.instanceDataId = instanceDataId;
    }
}
