package cn.sinozg.applet.turbo.engine.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class InstanceDataVo implements Serializable {
    private String key;
    private String type;
    private Object value;

    public InstanceDataVo() {
    }

    public InstanceDataVo(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public InstanceDataVo(String key, String type, Object value) {
        this.key = key;
        this.type = type;
        this.value = value;
    }
}
