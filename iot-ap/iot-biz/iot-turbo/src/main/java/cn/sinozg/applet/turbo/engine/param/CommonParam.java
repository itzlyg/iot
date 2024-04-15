package cn.sinozg.applet.turbo.engine.param;

import lombok.Data;

@Data
public class CommonParam {
    private String tenant;
    private String caller;

    public CommonParam(String tenant, String caller) {
        this.tenant = tenant;
        this.caller = caller;
    }
}
