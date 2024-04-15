package cn.sinozg.applet.controller.system;

import cn.sinozg.applet.biz.system.service.DictDataService;
import cn.sinozg.applet.biz.system.service.PwdUserLoginService;
import cn.sinozg.applet.biz.system.service.UserInfoService;
import cn.sinozg.applet.biz.system.vo.request.PasswordLoginRequest;
import cn.sinozg.applet.biz.system.vo.request.PasswordStrengthRequest;
import cn.sinozg.applet.biz.system.vo.request.UserInfoCodePasswordRequest;
import cn.sinozg.applet.biz.system.vo.response.DictListResponse;
import cn.sinozg.applet.common.core.base.BaseRequest;
import cn.sinozg.applet.common.core.base.BaseResponse;
import cn.sinozg.applet.common.enums.DictType;
import cn.sinozg.applet.common.utils.CipherStrength;
import cn.sinozg.applet.common.utils.MsgUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 不需要授权的接口信息
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-09 17:23
 */
@Slf4j
@RestController
@RequestMapping("/api/anon")
@Tag(name = "anon-controller", description = "不需要授权的接口信息")
public class AnonController {
    @Resource
    private PwdUserLoginService pwdUserLoginService;

    @Resource
    private DictDataService dictDataService;

    @Resource
    private UserInfoService userInfoService;

    /**
     * 获取到所有的租户信息，下拉列表
     *
     * @param request 查询参数
     * @return 数据字典
     */
    @PostMapping("/corps")
    @Operation(summary = "获取到所有的租户信息，下拉列表")
    public BaseResponse<List<DictListResponse>> dictList(@RequestBody @Valid BaseRequest<String> request) {
        List<DictListResponse> result = dictDataService.dictList(DictType.TENANT.getCode());
        return MsgUtil.ok(result);
    }

    /**
     * 登录方法
     *
     * @param request 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    @Operation(summary = "账号密码登录")
    public BaseResponse<String> login(@RequestBody @Valid BaseRequest<PasswordLoginRequest> request) {
        PasswordLoginRequest params = MsgUtil.params(request);
        pwdUserLoginService.passwordLogin(params.getUserName(), params.getPassword(), params);
        return MsgUtil.ok();
    }


    @PostMapping("/pwd/codePwd")
    @Operation(summary = "使用手机号或者邮箱的验证码获取修改密码")
    public BaseResponse<String> codePwd (@RequestBody @Valid BaseRequest<UserInfoCodePasswordRequest> request){
        UserInfoCodePasswordRequest params = MsgUtil.params(request);
        userInfoService.codePwd(params);
        return MsgUtil.ok();
    }

    @PostMapping("/pwd/strength")
    @Operation(summary = "密码强度检验 EASY MEDIUM STRONG VERY_STRONG EXTREMELY_STRONG ")
    public BaseResponse<String> strength (@RequestBody @Valid BaseRequest<PasswordStrengthRequest> request){
        PasswordStrengthRequest params = MsgUtil.params(request);
        String strength = CipherStrength.passwordStrengthLevel(params.getPassword());
        return MsgUtil.ok(strength);
    }
}
