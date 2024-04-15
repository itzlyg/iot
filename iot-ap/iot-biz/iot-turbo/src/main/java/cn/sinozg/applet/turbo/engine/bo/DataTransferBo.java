package cn.sinozg.applet.turbo.engine.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * The data transfer object is used in the scenario of parent to child
 * and child to parent of the CallActivity.
 * <p>
 * 1.parent to child
 * 2.child to parent
 */
@Getter
@Setter
public class DataTransferBo {

    private String sourceType;
    private String sourceKey;
    private String sourceValue;
    private String targetKey;
}
