package cn.sinozg.applet.biz.system.mapper;

import cn.sinozg.applet.biz.system.entity.UserRole;
import cn.sinozg.applet.common.constant.Constants;
import cn.sinozg.applet.common.core.model.RoleInfoVo;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* 用户角色表 Mapper 接口
* @Description
* @Copyright Copyright (c) 2023
* @author xyb
* @since 2023-04-11 20:40:45
*/
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 用户角色
     * @param userId 用户id
     * @return 角色信息
     */
    @InterceptorIgnore(tenantLine = Constants.IGNORE)
    List<RoleInfoVo> userRoleInfo (String userId);

    /**
     * 根据角色code 删除映射
     * @param roleCode 角色code
     * @return 记录数
     */
    long deleteByRoleCode(String roleCode);
}
