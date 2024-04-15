package cn.sinozg.applet.biz.system.service.impl;

import cn.sinozg.applet.biz.framework.service.FrameworkUserLoginService;
import cn.sinozg.applet.biz.system.enums.AuthType;
import cn.sinozg.applet.biz.system.service.UserInfoService;
import cn.sinozg.applet.biz.system.vo.request.PasswordLoginRequest;
import cn.sinozg.applet.biz.system.vo.response.LoginUserQueryResponse;
import cn.sinozg.applet.common.core.model.LoginUserVo;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.common.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 登录相关的接口实现
 * @Author: xyb
 * @Description:
 * @Date: 2023-03-24 下午 12:35
 **/
@Service
public class FrameworkUserLoginServiceImpl implements FrameworkUserLoginService {

    @Resource
    private UserInfoService userInfoService;

    /**
     * 查询到的用户信息，用于缓存
     * @param id 用户id
     * @return 用户信息
     */
    @Override
    public LoginUserVo userInfo(String id) {
        LoginUserVo user = userInfoService.userInfo(id);
        user.setAvatar(StringUtil.showUrl(user.getAvatar()));
        return user;
    }

    /**
     * 密码登录查询到用户信息
     * @param userName 用户名称
     * @param authType 授权模式
     * @param params 其他参数
     * @return 登陆用的信息
     */
    @Override
    public LoginUserQueryResponse loginUserInfoByPwd(String userName, String authType, Object params) {
        PasswordLoginRequest request = PojoUtil.cast(params);
        return userInfoService.loginUserInfoByPwd(request);
    }

    /**
     *
     * @param params 登录参数
     * @return 授权模式
     */
    @Override
    public AuthType beforeLogin(Object params) {
        return AuthType.PASSWORD;
    }

}