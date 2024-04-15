package cn.sinozg.applet.biz.system.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
* 用户信息表表 新增请求参数
* @Description
* @Copyright Copyright (c) 2023
* @author xyb
* @since 2023-03-24 14:52:38
*/
@Data
@Schema(name = "UserInfoStatusRequest", description = "用户信息表 状态修改")
public class UserInfoStatusRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @Schema(description = "用户ID")
    @NotBlank(message = "id不能为空")
    private String id;


    /** 帐号状态;00正常 01停用 */
    @NotBlank(message = "帐号状态不能为空")
    @Schema(description = "帐号状态;00正常 01停用")
    private String dataStatus;
}
