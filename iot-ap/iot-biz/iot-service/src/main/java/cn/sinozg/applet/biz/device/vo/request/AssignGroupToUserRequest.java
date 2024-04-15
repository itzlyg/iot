package cn.sinozg.applet.biz.device.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
* 设备分组信息表表 修改请求参数
* @Description
* @Copyright Copyright (c) 2023
* @author wang.pf
* @since 2023-12-03 16:27:01
*/
@Data
@Schema(name = "AssignGroupToUserRequest", description = "设备分组信息表 分配设备到用户请求参数")
public class AssignGroupToUserRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 主键id */
    @Schema(description = "主键id")
    @NotBlank(message = "id不能为空")
    private String id;


    /** 分组归属用户 */
    @Schema(description = "分组归属用户")
    private String groupOwner;

}
