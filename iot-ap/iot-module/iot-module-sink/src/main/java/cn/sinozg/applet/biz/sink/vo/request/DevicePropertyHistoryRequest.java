package cn.sinozg.applet.biz.sink.vo.request;

import lombok.Data;

/**
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-12-06 17:00:30
 */
@Data
public class DevicePropertyHistoryRequest {
    private String deviceCode;

    private String prodKey;
    private String name;

    private long start;
    private long end;
    private long size;
}
