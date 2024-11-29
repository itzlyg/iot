package cn.sinozg.applet.biz.notify.model.channel;

import cn.sinozg.applet.biz.notify.enums.NotifierType;
import cn.sinozg.applet.common.annotation.DictTrans;
import cn.sinozg.applet.common.enums.DictType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 媒介配置
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-26 16:40
 */
@Getter
@Setter
public class NotifierConfigBaseInfo {

    @Schema(description = "id")
    private String id;
    /**
     * 通道媒介类型
     * @see NotifierType#getCode()
     */
    @Schema(description = "通道媒介类型")
    @DictTrans(type = DictType.ALERTER_CHANNEL_TYPE)
    @NotBlank(message = "媒介类型不能为空！")
    private String type;

    /** 通道媒介名称 */
    @Schema(description = "通道媒介名称")
    private String channelName;
}
