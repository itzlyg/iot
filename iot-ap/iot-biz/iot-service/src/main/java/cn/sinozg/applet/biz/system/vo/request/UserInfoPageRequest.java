package cn.sinozg.applet.biz.system.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
* 用户信息表表 分页请求参数
* @Description
* @Copyright Copyright (c) 2023
* @author xyb
* @since 2023-03-24 14:52:38
*/
@Data
@Schema(name = "UserInfoPageRequest", description = "用户信息表 分页请求参数")
public class UserInfoPageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户名称")
    private String nickName;

    @Schema(description = "部门id")
    private String deptId;

    /** 角色code */
    @Schema(description = "角色code")
    private String roleCode;

    @Schema(description = "用户状态")
    private String dataStatus;

    @Schema(description = "手机号码")
    private String phoneNum;

    @Schema(description = "开始时间 yyyy-MM-dd")
    private String beginTime;

    @Schema(description = "结束时间 yyyy-MM-dd")
    private String endTime;

}
