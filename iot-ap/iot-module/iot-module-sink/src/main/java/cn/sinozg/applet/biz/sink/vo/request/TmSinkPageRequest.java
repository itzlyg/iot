package cn.sinozg.applet.biz.sink.vo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-06 18:56
 */
@Data
public class TmSinkPageRequest {

    @NotBlank(message = "设备编号不能为空！")
    private String deviceCode;
    private String type;
    private String identifier;
}
