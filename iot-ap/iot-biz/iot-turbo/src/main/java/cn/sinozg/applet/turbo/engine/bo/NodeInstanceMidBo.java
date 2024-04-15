package cn.sinozg.applet.turbo.engine.bo;

import cn.sinozg.applet.turbo.engine.result.RuntimeResult;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
public class NodeInstanceMidBo extends ElementInstance {
    private String nodeInstanceId;
    private int flowElementType;
    private List<RuntimeResult> subNodeResultList;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
}
