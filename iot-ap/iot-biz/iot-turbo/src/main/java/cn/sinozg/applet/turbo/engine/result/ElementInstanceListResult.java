package cn.sinozg.applet.turbo.engine.result;

import cn.sinozg.applet.turbo.engine.bo.ElementInstance;
import cn.sinozg.applet.turbo.engine.common.ErrorEnum;

import java.util.List;

public class ElementInstanceListResult extends CommonResult {
    private List<ElementInstance> elementInstanceList;

    public ElementInstanceListResult(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public List<ElementInstance> getElementInstanceList() {
        return elementInstanceList;
    }

    public void setElementInstanceList(List<ElementInstance> elementInstanceList) {
        this.elementInstanceList = elementInstanceList;
    }
}
