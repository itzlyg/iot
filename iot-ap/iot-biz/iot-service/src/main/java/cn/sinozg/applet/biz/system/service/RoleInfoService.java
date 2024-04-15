package cn.sinozg.applet.biz.system.service;

import cn.sinozg.applet.biz.system.entity.RoleInfo;
import cn.sinozg.applet.biz.system.vo.request.RoleInfoAddRequest;
import cn.sinozg.applet.biz.system.vo.request.RoleInfoPageRequest;
import cn.sinozg.applet.biz.system.vo.request.RoleInfoRequest;
import cn.sinozg.applet.biz.system.vo.request.RoleMenuAddRequest;
import cn.sinozg.applet.biz.system.vo.request.RoleMenuQueryRequest;
import cn.sinozg.applet.biz.system.vo.response.RoleInfoPageResponse;
import cn.sinozg.applet.biz.system.vo.response.RoleInfoResponse;
import cn.sinozg.applet.biz.system.vo.response.UserRoleResponse;
import cn.sinozg.applet.common.core.base.BasePageRequest;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* 角色信息表 服务类
* @Description
* @Copyright Copyright (c) 2023
* @author xyb
* @since 2023-04-11 20:40:45
*/
public interface RoleInfoService extends IService<RoleInfo> {

    /**
     * 通过角色code 找到角色名称
     * @param keys 角色code
     * @return 映射
     */
    Map<String, String> roleKeyNameMap (List<String> keys);


    /**
     * 获取角色已有的菜单
     * @param roleCode 角色编码
     * @return 菜单信息
     */
    List<String> roleCheckedMenu(RoleMenuQueryRequest roleCode);

    /**
     /**保存角色的菜单
     *
     * @param params 请求参数
     */
    void saveRoleMenu(RoleMenuAddRequest params);

    /**
     * 新增角色
     * @param request 请求参数
     */
    void addRole(RoleInfoAddRequest request);

    /**
     * 修改角色
     * @param params 请求参数
     */
    void updateRole(RoleInfoRequest params);

    /**
     * 删除角色
     * @param roleId 角色id
     */
    void deleteRole(String roleId);

    /**
     *  查看角色详情
     * @param roleId 角色id
     * @return 角色信息
     */
    RoleInfoResponse detail(String roleId);

    /**
     * 分页查询角色信息
     * @param request 请求参数
     * @return 分页信息
     */
    BasePageResponse<List<RoleInfoPageResponse>> pageList(BasePageRequest<RoleInfoPageRequest> request);

    /**
     * 查询所有的用户角色
     * @return 角色信息
     */
    List<UserRoleResponse> userRoles ();

    /**
     * 角色code
     * @param roleCode 角色code
     * @return 角色
     */
    List<RoleInfo> roleInfo(List<String> roleCode);
}
