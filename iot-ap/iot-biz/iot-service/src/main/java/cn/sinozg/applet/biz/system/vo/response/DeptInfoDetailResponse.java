package cn.sinozg.applet.biz.system.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 部门详情
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-09-13 11:19
 */
@Data
public class DeptInfoDetailResponse {
    /** 主键id */
    @Schema(description = "主键id")
    private String id;

    /** 父部门id */
    @Schema(description = "父部门id")
    private String paterId;

    /** 部门名称 */
    @Schema(description = "部门名称")
    private String deptName;

    /** 负责人 */
    @Schema(description = "负责人")
    private String director;

    /** 负责人姓名 */
    @Schema(description = "负责人姓名")
    private String directorName;

    /** 联系方式 */
    @Schema(description = "联系方式")
    private String phoneNum;

    /** 邮箱 */
    @Schema(description = "邮箱")
    private String email;
}
