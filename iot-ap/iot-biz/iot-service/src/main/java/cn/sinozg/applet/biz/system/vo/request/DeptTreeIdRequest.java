package cn.sinozg.applet.biz.system.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* 校区信息表 新增请求参数
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-09-06 11:22:14
*/
@Data
@Schema(description = "校区信息 新增请求参数")
public class DeptTreeIdRequest {

    /** 父部门id */
    @Schema(description = "父部门id")
    private String paterId;
}
