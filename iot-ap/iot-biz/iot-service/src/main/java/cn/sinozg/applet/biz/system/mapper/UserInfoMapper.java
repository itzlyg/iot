package cn.sinozg.applet.biz.system.mapper;

import cn.sinozg.applet.biz.system.entity.UserInfo;
import cn.sinozg.applet.biz.system.vo.request.PasswordLoginRequest;
import cn.sinozg.applet.biz.system.vo.request.UserInfoPageRequest;
import cn.sinozg.applet.biz.system.vo.response.DictListResponse;
import cn.sinozg.applet.biz.system.vo.response.LoginUserQueryResponse;
import cn.sinozg.applet.biz.system.vo.response.UserInfoPageResponse;
import cn.sinozg.applet.common.constant.Constants;
import cn.sinozg.applet.common.core.model.LoginUserVo;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 用户信息表 Mapper 接口
* @Description
* @Copyright Copyright (c) 2023
* @author xyb
* @since 2023-03-24 14:52:38
*/
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    /**
     *
     * @param id id
     * @return 用户
     */
    @InterceptorIgnore(tenantLine = Constants.IGNORE)
    LoginUserVo userInfo(String id);

    /**
     * 获取密码
     * @param id id
     * @return 密码等信息
     */
    @InterceptorIgnore(tenantLine = Constants.IGNORE)
    LoginUserVo passwordInfo (String id);

    /**
     * 查询登陆信息
     * @param params 用户信息
     * @return 用户
     */
    @InterceptorIgnore(tenantLine = Constants.IGNORE)
    LoginUserQueryResponse loginUserInfoByPwd(@Param("p") PasswordLoginRequest params);

    /**
     * 查询系统登录日志集合
     * @param page 分页
     * @param params 用户请求参数
     * @return 用户记录集合
     */
    IPage<UserInfoPageResponse> userInfoPage(Page<UserInfoPageResponse> page, @Param("p") UserInfoPageRequest params);

    /**
     * 查询某个部门下所有的员工
     * @param deptId 部门id
     * @return 员工信息
     */
    List<DictListResponse> userSelectByDept(String deptId);
}
