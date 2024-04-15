package cn.sinozg.applet.iot.protocol.vo.response;

import lombok.Data;


/**
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-11-16 11:20:07
 */
@Data
public class DeviceOtaDetailResponse {

    private String id;

    private Integer step;

    private String taskId;

    private String desc;

    private String version;

    private String module;

    private String deviceCode;

    private String prodKey;

    private String deviceName;

    private Long otaInfoId;
}
