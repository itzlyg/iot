package cn.sinozg.applet.controller.system;

import cn.sinozg.applet.biz.system.service.UserLoginOutService;
import cn.sinozg.applet.common.core.base.BaseRequest;
import cn.sinozg.applet.common.core.base.BaseResponse;
import cn.sinozg.applet.common.utils.MsgUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
* 登出信息
* @Author: xyb
* @Description:
* @Date: 2023-03-06 上午 12:19
**/
@Slf4j
@RestController
@RequestMapping("/api/logout")
@Tag(name = "login-out-controller", description = "登出系统")
public class LoginOutController {
    @Resource
    private UserLoginOutService loginOutService;

    /**
     * 强退用户
     */
    @PostMapping("/force_logout")
    @Operation(summary = "强制踢人下线，通过 用户id")
    public BaseResponse<String> forceLogout(@RequestBody @Valid BaseRequest<String> param) {
        String userId = MsgUtil.params(param);
        // 强制指定 用户id 全部 注销下线
        loginOutService.logout(null, userId);
        return MsgUtil.ok();
    }

    @PostMapping("/logout")
    @Operation(summary = "用户退出登录")
    public BaseResponse<String> logout(HttpServletRequest request) {
        loginOutService.logout(null, null);
        return MsgUtil.ok();
    }
}
