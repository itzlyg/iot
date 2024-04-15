package cn.sinozg.applet.biz.open.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ReportCallbackRequest {
//    /** 验证的消息 */
//    @NotBlank(message = "验证消息不能为空")
//    @Schema(description = "验证的消息")
//    private String echostr;

    /** 加密后的信息 */
    @NotBlank(message = "加密后的信息不能为空")
    @Schema(description = "json数据 加密后的信息")
    private String signature;

//    /** 时间戳 */
//    @NotBlank(message = "时间戳不能为空")
//    @Schema(description = "时间戳")
//    private String timestamp;

//    /** 随机数 */
//    @NotBlank(message = "随机数不能为空")
//    @Schema(description = "随机数")
//    private String nonce;
}
