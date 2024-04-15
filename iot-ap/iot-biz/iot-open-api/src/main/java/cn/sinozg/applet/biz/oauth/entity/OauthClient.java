package cn.sinozg.applet.biz.oauth.entity;

import cn.sinozg.applet.common.core.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 授权客户端表
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-03-09 16:58:31
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_oauth_client")
@Schema(name = "OauthClient", description = "授权客户端")
public class OauthClient extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /** 应用id */
    @Schema(description = "应用id")
    @TableField("client_id")
    private String clientId;


    /** 应用秘钥 */
    @Schema(description = "应用秘钥")
    @TableField("client_secret")
    private String clientSecret;


    /** openId */
    @Schema(description = "openId")
    @TableField("open_id")
    private String openId;


    /** 回调地址 */
    @Schema(description = "回调地址")
    @TableField("redirect_uri")
    private String redirectUri;


    /** 允许授权的所有URL */
    @Schema(description = "允许授权的所有URL")
    @TableField("allow_url")
    private String allowUrl;


    /** 授权模式 */
    @Schema(description = "授权模式")
    @TableField("mode")
    private String mode;


    /** 签约的所有权限 多个用逗号隔开 */
    @Schema(description = "签约的所有权限 多个用逗号隔开")
    @TableField("scopes")
    private String scopes;


    /** access token有效期秒 默认 24小时 */
    @Schema(description = "access token有效期秒 默认 24小时")
    @TableField("access_token_timeout")
    private Long accessTokenTimeout;


    /** refresh token有效期秒 默认 30天 */
    @Schema(description = "refresh token有效期秒 默认 30天")
    @TableField("refresh_token_timeout")
    private Long refreshTokenTimeout;


    /** 绑定用户 */
    @Schema(description = "绑定用户")
    @TableField("bind_user")
    private String bindUser;
}
