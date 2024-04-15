package cn.sinozg.applet.biz.sink.model.response;

import lombok.Data;

/**
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-12-06 17:00:30
 */
@Data
public class TbDevicePropertyResponse {

    private Long ts;

    private String deviceCode;

    private String name;

    private Object value;
}
