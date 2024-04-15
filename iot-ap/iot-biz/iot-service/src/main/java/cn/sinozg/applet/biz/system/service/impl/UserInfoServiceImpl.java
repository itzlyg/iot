package cn.sinozg.applet.biz.system.service.impl;

import cn.sinozg.applet.biz.system.entity.UserInfo;
import cn.sinozg.applet.biz.system.mapper.UserInfoMapper;
import cn.sinozg.applet.biz.system.service.UserInfoService;
import cn.sinozg.applet.biz.system.service.UserRoleService;
import cn.sinozg.applet.biz.system.vo.request.PasswordLoginRequest;
import cn.sinozg.applet.biz.system.vo.request.UserInfoCodePasswordRequest;
import cn.sinozg.applet.biz.system.vo.request.UserInfoCreateRequest;
import cn.sinozg.applet.biz.system.vo.request.UserInfoPageRequest;
import cn.sinozg.applet.biz.system.vo.request.UserInfoUpdatePasswordRequest;
import cn.sinozg.applet.biz.system.vo.request.UserInfoUpdateRequest;
import cn.sinozg.applet.biz.system.vo.response.DictListResponse;
import cn.sinozg.applet.biz.system.vo.response.LoginUserQueryResponse;
import cn.sinozg.applet.biz.system.vo.response.UserInfoDetailResponse;
import cn.sinozg.applet.biz.system.vo.response.UserInfoPageResponse;
import cn.sinozg.applet.common.constant.Constants;
import cn.sinozg.applet.common.constant.RedisKey;
import cn.sinozg.applet.common.core.base.BasePageRequest;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.model.LoginUserVo;
import cn.sinozg.applet.common.core.model.RoleInfoVo;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.CipherStrength;
import cn.sinozg.applet.common.utils.CypherUtil;
import cn.sinozg.applet.common.utils.MsgUtil;
import cn.sinozg.applet.common.utils.PageUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.common.utils.RedisUtil;
import cn.sinozg.applet.common.utils.UserUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.Function;

/**
* 用户信息表 服务实现类
* @Description
* @Copyright Copyright (c) 2023
* @author xyb
* @since 2023-03-24 14:52:38
*/
@Slf4j
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Resource
    private UserInfoMapper mapper;

    @Resource
    private UserRoleService userRoleService;

    @Override
    public LoginUserVo userInfo(String id) {
        LoginUserVo user = mapper.userInfo(id);
        if (user == null) {
            throw new CavException("用户信息不存在！");
        }
        List<RoleInfoVo> roles = userRoleService.userRoleInfo(id);
        List<String> list = PojoUtil.toList(roles, RoleInfoVo::getRoleKey);
        user.setRoleInfos(roles);
        user.setRoles(list);
        return user;
    }

    @Override
    public LoginUserVo passwordInfo(String id) {
        return mapper.passwordInfo(id);
    }

    @Override
    public LoginUserQueryResponse loginUserInfoByPwd(PasswordLoginRequest params) {
        return mapper.loginUserInfoByPwd(params);
    }

    @Override
    public BasePageResponse<List<UserInfoPageResponse>> userPageInfo(BasePageRequest<UserInfoPageRequest> request) {
        UserInfoPageRequest params = MsgUtil.params(request);
        PageUtil<UserInfoPageResponse, UserInfoPageRequest> pu = (p, q) -> mapper.userInfoPage(p, q);
        return pu.page(request.getPage(), params);
    }

    @Override
    public void deleteUserInfo(String id) {
        UserInfo info = userDetail(id);
        this.removeById(info.getId());
        // 删除对应的角色
        userRoleService.deleteUserRoles(id);
    }

    @Override
    public void updateUserStatus(String userId, String status) {
        UserInfo info = userDetail(userId);
        if (status.equals(info.getDataStatus())) {
            throw new CavException("状态未改变！");
        }
        UserInfo updateInfo = new UserInfo();
        updateInfo.setId(userId);
        updateInfo.setDataStatus(status);
        this.updateById(updateInfo);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserInfo(UserInfoUpdateRequest updateRequest) {
        UserInfo info = PojoUtil.copyBean(updateRequest, UserInfo.class);
        checkUserUniqueness(info);
        this.updateById(info);
        // 保存角色
        userRoleService.addUserRoles(info.getId(), updateRequest.getRoleIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUserInfo(UserInfoCreateRequest createRequest) {
        UserInfo info = PojoUtil.copyBean(createRequest, UserInfo.class);
        checkUserUniqueness(info);
        this.save(info);
        // 保存角色
        userRoleService.addUserRoles(info.getId(), createRequest.getRoleIds());
    }

    @Override
    public UserInfoDetailResponse userInfoDetail(String id) {
        UserInfo info = userDetail(id);
        UserInfoDetailResponse response = PojoUtil.copyBean(info, UserInfoDetailResponse.class);
        List<String> roles = userRoleService.userRoles(id);
        response.setRoleIds(roles);
        response.setAvatarUrl(response.getAvatar());
        return response;
    }

    /**
     * 重置用户密码
     * @param id 用户id
     * @param newPassword 新密码
     */
    @Override
    public void resetPassword(String id, String newPassword){
        UserInfo info = userDetail(id);
        savePassword(info.getId(), newPassword);
    }

    /**
     * 用用户密码的方式去更新用户密码
     * @param params 请求参数
     */
    @Override
    public void updatePassword (UserInfoUpdatePasswordRequest params){
        String id = UserUtil.uid();
        UserInfo info = userDetail(id);
        if (!CypherUtil.matches(params.getOldPassword(), info.getPassWord())) {
            throw new CavException("密码输入错误！");
        }
        updatePassword(info, params.getNewPassword(), params.getConfirmPassword());
    }

    /**
     * 使用 手机验证码 或者邮箱验证码去更新用户密码
     * @param params 请求参数
     */
    @Override
    public void codePwd(UserInfoCodePasswordRequest params) {
        String emailPhone = params.getEmailPhone();
        UserInfo query = new UserInfo();
        query.setEmail(emailPhone);
        query.setPhoneNum(emailPhone);
        String redisCode = RedisUtil.getCacheObject(RedisKey.CAPTCHA_CODE_KEY + emailPhone);
        if (StringUtils.isBlank(redisCode)) {
            throw new CavException("验证码已经失效！");
        }
        if (StringUtils.equals(redisCode, params.getCode())) {
            throw new CavException("验证码不正确！");
        }
        UserInfo info = lambdaQueryUser(query, false).get(0);
        updatePassword(info, params.getNewPassword(), params.getConfirmPassword());
    }

    @Override
    public List<UserInfo> userByDeptId(String deptId) {
        return this.list(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getDeptId, deptId));
    }

    @Override
    public List<DictListResponse> userSelectByDept(String deptId) {
        if (StringUtils.isBlank(deptId)) {
            deptId = Constants.DICT_ROOT;
        }
        return mapper.userSelectByDept(deptId);
    }
    /**
     * 更新用户密码
     * @param info 用户信息
     * @param newPassword 新密码
     * @param confirmPassword 确认密码
     */
    private void updatePassword (UserInfo info, String newPassword, String confirmPassword){
        if (!newPassword.equals(confirmPassword)) {
            throw new CavException("两次密码不一致！");
        }
        if (CipherStrength.weakPassword(newPassword)) {
            throw new CavException("密码强度太弱！");
        }
        if (CypherUtil.matches(newPassword, info.getPassWord())) {
            throw new CavException("新密码不能与旧密码一致！");
        }
        savePassword(info.getId(), newPassword);
    }


    /**
     * 保存密码到数据库
     * @param id 用户id
     * @param password 密码
     */
    private void savePassword (String id, String password){
        UserInfo updateInfo = new UserInfo();
        updateInfo.setId(id);
        updateInfo.setPassWord(CypherUtil.encoder(password));
        this.updateById(updateInfo);
    }

    /**
     * 检查用户的唯一性
     * @param info 用户查询参数
     */
    private void checkUserUniqueness (UserInfo info){
        // email phone username 唯一
        List<UserInfo> list = lambdaQueryUser(info, true);
        if (CollectionUtils.isNotEmpty(list)) {
            for (UserInfo db : list) {
                if (existUser(db, info, UserInfo::getUserName)) {
                    throw new CavException("用户名{}已经存在！", info.getUserName());
                }
                if (existUser(db, info, UserInfo::getEmail)) {
                    throw new CavException("邮箱{}已经存在！", info.getEmail());
                }
                if (existUser(db, info, UserInfo::getPhoneNum)) {
                    throw new CavException("手机{}已经存在！", info.getPhoneNum());
                }
            }
        }
    }

    /**
     * 查询用户信息
     * @param info 查询条件
     * @param onlyQuery 是否只是查询
     * @return 用户信息
     */
    private List<UserInfo> lambdaQueryUser (UserInfo info, boolean onlyQuery){
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<UserInfo>()
                .ne(StringUtils.isNotBlank(info.getId()), UserInfo::getId, info.getId())
                .and(w -> w.eq(StringUtils.isNotBlank(info.getEmail()), UserInfo::getEmail, info.getEmail())
                        .or().eq(StringUtils.isNotBlank(info.getPhoneNum()), UserInfo::getPhoneNum, info.getPhoneNum())
                        .or().eq(StringUtils.isNotBlank(info.getUserName()), UserInfo::getUserName, info.getUserName())
                );
        List<UserInfo> list = this.list(wrapper);
        if (!onlyQuery && list.size() != 1) {
            throw new CavException("用户不存在或者存在多个！");
        }
        return list;
    }

    /**
     * 判断属性是否存在
     * @param db 数据库的值
     * @param form 表单值
     * @param function 取值过程
     * @return 是否存在
     */
    private boolean existUser(UserInfo db, UserInfo form, Function<UserInfo, String> function){
        String formValue = function.apply(form);
        return StringUtils.isNotBlank(formValue) && formValue.equals(function.apply(db));
    }

    private UserInfo userDetail (String id){
        UserInfo info = this.getById(id);
        if (info == null) {
            throw new CavException("用户信息为空！");
        }
        return info;
    }
}
