package cn.sinozg.applet.biz.sink.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 物模型 信息 用来接收消息 保存
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-06 18:56
 */
@Data
public class TmSinkAddRequest {

    private String mid;

    private String deviceCode;

    private String prodKey;

    private String deviceName;

    @Schema(description = "所属用户ID")
    private String uid;

    private String type;

    private String identifier;

    @Schema(description = "消息状态码")
    private int code;

    private Object data;

    @Schema(description = "时间戳，设备上的事件或数据产生的本地时间")
    private Long occurred;

    @Schema(description = "消息上报时间")
    private Long ts;
}
