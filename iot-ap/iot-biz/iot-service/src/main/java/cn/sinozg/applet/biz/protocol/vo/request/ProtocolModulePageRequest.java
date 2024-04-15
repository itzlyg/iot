package cn.sinozg.applet.biz.protocol.vo.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
* 协议组件信息表 分页请求参数
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
@Data
@Schema(name = "ProtocolModulePageRequest", description = "协议组件信息 分页请求参数")
public class ProtocolModulePageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private String uid;

    @Schema(description = "协议名称")
    private String protocolName;

    @Schema(description = "开始时间 yyyy-MM-dd")
    private String beginTime;

    @Schema(description = "结束时间 yyyy-MM-dd")
    private String endTime;
}
