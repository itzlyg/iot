package cn.sinozg.applet.biz.product.vo.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
* 产品消息表表 分页请求参数
* @Description
* @Copyright Copyright (c) 2023
* @author zy
* @since 2023-11-27 21:44:29
*/
@Data
@Schema(name = "ProdInfoPageRequest", description = "产品消息表 分页请求参数")
public class ProdInfoPageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private String uid;

    @Schema(description = "开始时间 yyyy-MM-dd")
    private String beginTime;

    @Schema(description = "结束时间 yyyy-MM-dd")
    private String endTime;
}
