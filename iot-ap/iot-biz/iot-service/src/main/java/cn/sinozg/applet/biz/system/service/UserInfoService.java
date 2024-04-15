package cn.sinozg.applet.biz.system.service;

import cn.sinozg.applet.biz.system.entity.UserInfo;
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
import cn.sinozg.applet.common.core.base.BasePageRequest;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.model.LoginUserVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* 用户信息表 服务类
* @Description
* @Copyright Copyright (c) 2023
* @author xyb
* @since 2023-03-24 14:52:38
*/
public interface UserInfoService extends IService<UserInfo> {

    LoginUserVo userInfo(String id);

    /**
     * 获取密码
     * @param id id
     * @return 密码等信息
     */
    LoginUserVo passwordInfo (String id);

    LoginUserQueryResponse loginUserInfoByPwd(PasswordLoginRequest params);

    BasePageResponse<List<UserInfoPageResponse>> userPageInfo(BasePageRequest<UserInfoPageRequest> request);

    void deleteUserInfo(String id);

    void updateUserStatus(String userId, String status);

    void updateUserInfo (UserInfoUpdateRequest updateRequest);

    void insertUserInfo (UserInfoCreateRequest createRequest);

    UserInfoDetailResponse userInfoDetail (String id);

    void resetPassword(String id, String newPassword);

    void updatePassword (UserInfoUpdatePasswordRequest params);

    void codePwd(UserInfoCodePasswordRequest params);

    /**
     * 根据部门id 找到所有的员工
     * @param deptId 部门id
     * @return 员工
     */
    List<UserInfo> userByDeptId (String deptId);

    /**
     * 查询本部门已经本部门下的所有员工
     * @param deptId 部门id
     * @return 员工
     */
    List<DictListResponse> userSelectByDept(String deptId);
}
