package cn.sinozg.applet.biz.system.vo.request;

import cn.sinozg.applet.common.annotation.Regex;
import cn.sinozg.applet.common.enums.RegexType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
* 部门信息表 新增请求参数
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-09-06 11:22:14
*/
@Data
@Schema(name = "DeptInfoCreateRequest", description = "部门信息 新增请求参数")
public class DeptInfoCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 部门名称 */
    @Schema(description = "部门名称")
    @NotBlank(message = "部门名称不能为空")
    private String deptName;

    /** 父部门id */
    @Schema(description = "父部门id")
    private String paterId;

    /** 负责人 */
    @Schema(description = "负责人")
    @NotBlank(message = "负责人不能为空")
    private String director;

    /** 联系方式 */
    @Schema(description = "联系方式")
    private String phoneNum;


    /** 邮箱 */
    @Schema(description = "邮箱")
    @Regex(type = RegexType.EMAIL)
    private String email;
}
