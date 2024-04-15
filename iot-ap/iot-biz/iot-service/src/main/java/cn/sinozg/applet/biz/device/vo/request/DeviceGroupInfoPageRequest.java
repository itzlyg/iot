package cn.sinozg.applet.biz.device.vo.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
* 设备分组信息表表 分页请求参数
* @Description
* @Copyright Copyright (c) 2023
* @author wang.pf
* @since 2023-12-03 16:27:01
*/
@Data
@Schema(name = "DeviceGroupInfoPageRequest", description = "设备分组信息表 分页请求参数")
public class DeviceGroupInfoPageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private String uid;

    @Schema(description = "开始时间 yyyy-MM-dd")
    private String beginTime;

    @Schema(description = "结束时间 yyyy-MM-dd")
    private String endTime;

    @Schema(description = "分组名称")
    private String groupName;
}
