package cn.sinozg.applet.turbo.engine.bo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeInstanceBo {
    //used while updateById
    private String id;
    private String nodeInstanceId;
    private String nodeKey;
    private String sourceNodeInstanceId;
    private String sourceNodeKey;
    private String instanceDataId;
    private Integer status;
}
