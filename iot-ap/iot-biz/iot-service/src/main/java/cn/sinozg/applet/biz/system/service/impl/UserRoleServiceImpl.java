package cn.sinozg.applet.biz.system.service.impl;

import cn.sinozg.applet.biz.system.entity.UserRole;
import cn.sinozg.applet.biz.system.mapper.UserRoleMapper;
import cn.sinozg.applet.biz.system.service.RoleInfoService;
import cn.sinozg.applet.biz.system.service.UserRoleService;
import cn.sinozg.applet.common.core.model.RoleInfoVo;
import cn.sinozg.applet.common.utils.PojoUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
* 用户角色表 服务实现类
* @Description
* @Copyright Copyright (c) 2023
* @author xyb
* @since 2023-04-11 20:40:45
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Resource
    private UserRoleMapper mapper;

    @Resource
    private RoleInfoService roleInfoService;

    @Override
    public List<RoleInfoVo> userRoleInfo (String userId){
        return mapper.userRoleInfo(userId);
    }

    @Override
    public List<String> userRoles(String userId) {
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userId);
        List<UserRole> roles = this.list(wrapper);
        return PojoUtil.toList(roles, UserRole::getRoleCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserRoles(String userId, List<String> roleIds) {
        deleteUserRoles(userId);
        if (CollectionUtils.isNotEmpty(roleIds)) {
            List<UserRole> roles = new ArrayList<>();
            Map<String, String> map = roleInfoService.roleKeyNameMap(roleIds);
            roleIds.forEach(r -> {
                UserRole role = new UserRole();
                role.setRoleCode(r);
                role.setUserId(userId);
                role.setRoleName(map.get(r));
                roles.add(role);
            });
            this.saveBatch(roles);
        }
    }

    @Override
    public void deleteUserRoles(String userId) {
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userId);
        this.remove(wrapper);
    }

}
