package cn.sinozg.applet.biz.device.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
* 设备分组信息表表 分页请求参数
* @Description
* @Copyright Copyright (c) 2023
* @author wang.pf
* @since 2023-12-03 16:27:01
*/
@Data
@Schema(name = "AddDeviceGroupRequest", description = "设备分组信息表 分页请求参数")
public class AddDeviceGroupRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    @NotBlank(message = "id不能为空")
    private String id;

    @Schema(description = "需要添加的设备ID列表")
    @NotNull(message = "设备ID列表不能为空")
    private List<String> deviceCodeList;
}
