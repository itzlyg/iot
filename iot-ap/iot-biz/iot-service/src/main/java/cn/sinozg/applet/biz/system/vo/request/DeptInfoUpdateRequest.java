package cn.sinozg.applet.biz.system.vo.request;

import cn.sinozg.applet.common.annotation.Regex;
import cn.sinozg.applet.common.enums.RegexType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
* 部门信息表 修改请求参数
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-09-06 11:22:14
*/
@Data
@Schema(name = "DeptInfoUpdateRequest", description = "部门信息 修改请求参数")
public class DeptInfoUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 主键id */
    @Schema(description = "主键id")
    @NotBlank(message = "id不能为空！")
    private String id;

    /** 部门名称 */
    @Schema(description = "部门名称")
    @NotBlank(message = "部门名称不能为空！")
    private String deptName;

    /** 负责人 */
    @Schema(description = "负责人")
    private String director;

    /** 联系方式 */
    @Schema(description = "联系方式")
    @NotBlank(message = "负责人不能为空")
    private String phoneNum;


    /** 邮箱 */
    @Schema(description = "邮箱")
    @Regex(type = RegexType.EMAIL)
    private String email;
}
