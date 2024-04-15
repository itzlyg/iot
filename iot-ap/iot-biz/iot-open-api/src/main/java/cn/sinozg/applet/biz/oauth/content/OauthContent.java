package cn.sinozg.applet.biz.oauth.content;

/**
 * 常量
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-03-09 18:36
 */
public interface OauthContent {

    /** 类型 **/
    String TP = "tp";

    /** 本地登录 标记 **/
    String TP_FLAG = "j_z";
    /** 密码模式获取token **/
    String TOKEN_URL = "http://127.0.0.1:%d/oauth2/token?grant_type=password&client_id=%s&client_secret=%s&username=username&password=password&tp=%s";
    /** 刷新token **/
    String REFRESH_URL = "http://127.0.0.1:%d/oauth2/refresh?grant_type=refresh_token&client_id=%s&client_secret=%s&refresh_token=%s";

    /** client_id **/
    String CLIENT_ID = "client_id";
    /** access_token **/
    String ACCESS_TOKEN = "access_token";
    /** client_id **/
    String REFRESH_TOKEN = "refresh_token";
    /** expires_in **/
    String EXPIRES_IN = "expires_in";
    /** refresh_expires_in **/
    String REFRESH_EXPIRES_IN = "refresh_expires_in";
}
