package cn.sinozg.applet.biz.system.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-01 16:21
 */
@Data
public class CropCreateRequest {

    /** 租户名称 */
    @Schema(description = "租户名称")
    private String corpName;


    /** 联系人 */
    @Schema(description = "联系人")
    private String liaison;


    /** 联系电话 */
    @Schema(description = "联系电话")
    private String phoneNum;


    /** 邮箱地址 */
    @Schema(description = "邮箱地址")
    private String email;


    /** 联系地址 */
    @Schema(description = "联系地址")
    private String address;
}
