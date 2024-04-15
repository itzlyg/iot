package cn.sinozg.applet.iot.protocol.model;

import cn.sinozg.applet.biz.com.enums.TmIdentifierType;
import cn.sinozg.applet.biz.com.enums.TmType;
import cn.sinozg.applet.common.annotation.EnumField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-11-15 17:06:31
 */
@Data
public class ThingServiceParams<T> {

    @Schema(description = "mid，请求id")
    @NotBlank(message = "请求id不能为空")
    private String mid;

    @Schema(description = "产品key")
    @NotBlank(message = "产品key不能为空")
    private String prodKey;

    @Schema(description = "设备编号")
    @NotBlank(message = "设备编号不能为空")
    private String deviceCode;

    /**
     * 消息类型
     * @see TmType
     */
    @Schema(description = "消息类型")
    @EnumField(clazz = TmType.class, key = "enName")
    @NotBlank(message = "消息类型不能为空")
    private String type;

    /**
     * 事件标准符
     * @see TmIdentifierType
     */
    @EnumField(clazz = TmIdentifierType.class, key = "enName")
    @Schema(description = "事件标准符")
    @NotBlank(message = "事件标准符不能为空")
    private String identifier;

    @Schema(description = "具体消息对象 对象 或者 直接要发发送的")
    @NotNull(message = "消息对象不能为空")
    private T params;

    public void setType(String type) {
        this.type = type;
    }

    public void setType(TmType type) {
        this.type = type.getEnName();
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setIdentifier(TmIdentifierType identifier) {
        this.identifier = identifier.getEnName();
    }
}
