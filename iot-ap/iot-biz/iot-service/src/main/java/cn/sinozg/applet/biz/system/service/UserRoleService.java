package cn.sinozg.applet.biz.system.service;

import cn.sinozg.applet.biz.system.entity.UserRole;
import cn.sinozg.applet.common.core.model.RoleInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* 用户角色表 服务类
* @Description
* @Copyright Copyright (c) 2023
* @author xyb
* @since 2023-04-11 20:40:45
*/
public interface UserRoleService extends IService<UserRole> {

    List<RoleInfoVo> userRoleInfo (String userId);

    /**
     * 查询用户的角色信息
     * @param userId 用户id
     * @return 角色id
     */
    List<String> userRoles (String userId);

    /**
     * 修改用户角色信息
     * @param userId 用户id
     * @param roleIds 角色id
     * @return 角色
     */
    void addUserRoles (String userId, List<String> roleIds);

    /**
     * 删除用户的角色
     * @param userId
     */
    void deleteUserRoles (String userId);
}
