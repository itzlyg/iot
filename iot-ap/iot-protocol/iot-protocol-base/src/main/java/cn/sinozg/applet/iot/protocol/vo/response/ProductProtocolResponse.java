package cn.sinozg.applet.iot.protocol.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description 
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-11-16 11:17:33
 */
@Data
public class ProductProtocolResponse {

    @Schema(description = "产品key 即id")
    private String prodKey;

    @Schema(description = "产品密钥")
    private String prodSecret;

    @Schema(description = "产品名称")
    private String name;

    @Schema(description = "分类ID：用于对物体进行分类")
    private String classifyId;

    @Schema(description = "所属平台用户ID")
    private String uid;

    @Schema(description = "是否透传")
    private Boolean transparent;

    public boolean isTransparent() {
        return transparent != null && transparent;
    }
}