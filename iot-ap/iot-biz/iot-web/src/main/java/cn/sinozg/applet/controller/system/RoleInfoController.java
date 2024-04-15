package cn.sinozg.applet.controller.system;

import cn.sinozg.applet.biz.system.service.RoleInfoService;
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
import cn.sinozg.applet.common.core.base.BaseRequest;
import cn.sinozg.applet.common.core.base.BaseResponse;
import cn.sinozg.applet.common.core.model.ComId;
import cn.sinozg.applet.common.utils.MsgUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 前端控制器
 *
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2022
 * @since 2022-07-14 10:18:46
 */
@RestController
@RequestMapping("/api/role/info")
@Tag(name = "role-info-controller", description = "角色信息表接口")
public class RoleInfoController {

    @Resource
    private RoleInfoService roleInfService;


    /**
     * 角色拥有的菜单
     * @param request 请求参数
     * @return
     */
    @PostMapping("/role_menu")
    @Operation(summary = "角色拥有的菜单")
    public BaseResponse<List<String>> roleMenu(@RequestBody @Valid BaseRequest<RoleMenuQueryRequest> request) {
        List<String> menus = roleInfService.roleCheckedMenu(request.getParams());
        return MsgUtil.ok(menus);
    }

    /**
     * 保存角色的菜单
     * @param request
     * @return
     */
    @PostMapping("/add_role_menu")
    @Operation(summary = "保存角色菜单")
    public BaseResponse<String> addRoleMenu(@RequestBody @Valid BaseRequest<RoleMenuAddRequest> request) {
        RoleMenuAddRequest params = MsgUtil.params(request);
        roleInfService.saveRoleMenu(params);
        return MsgUtil.ok();
    }


    /**
     * 新增角色
     * @param request 查询参数
     * @return
     */
    @PostMapping("/add")
    @Operation(summary = "新增角色")
    public BaseResponse<String> addRoleInfo(@RequestBody @Valid BaseRequest<RoleInfoAddRequest> request) {
        RoleInfoAddRequest params = MsgUtil.params(request);
        roleInfService.addRole(params);
        return MsgUtil.ok();
    }

    /**
     * @desc: 修改角色
     **/
    @PostMapping("/update")
    @Operation(summary = "修改角色信息")
    public BaseResponse<String> updateRoleInfo(@RequestBody @Valid BaseRequest<RoleInfoRequest> request) {
        RoleInfoRequest params = MsgUtil.params(request);
        roleInfService.updateRole(params);
        return MsgUtil.ok();
    }

    /**
     * 删除角色
     *
     * @param request 查询参数
     * @return
     */
    @PostMapping("/delete")
    @Operation(summary = "删除角色")
    public BaseResponse<String> deleteRoleInfo(@RequestBody @Valid BaseRequest<ComId> request) {
        ComId params = MsgUtil.params(request);
        roleInfService.deleteRole(params.getId());
        return MsgUtil.ok();
    }

    /**
     * 角色详情
     *
     * @param request 查询参数
     * @return
     */
    @PostMapping("/detail")
    @Operation(summary = "查看角色详情")
    public BaseResponse<RoleInfoResponse> detailOfRoleInfo(@RequestBody @Valid BaseRequest<ComId> request) {
        ComId params = MsgUtil.params(request);
        RoleInfoResponse detail = roleInfService.detail(params.getId());
        return MsgUtil.ok(detail);
    }

    /**
     * 角色管理分页
     * @param request
     * @return
     */
    @PostMapping("/page")
    @Operation(summary = "角色管理分页")
    public BasePageResponse<List<RoleInfoPageResponse>> pageOfRoleInfo(@RequestBody @Valid BasePageRequest<RoleInfoPageRequest> request) {
        return roleInfService.pageList(request);
    }


    @PostMapping("/user_role")
    @Operation(summary = "查询用户所有的角色")
    public BaseResponse<List<UserRoleResponse>> userRoles(@RequestBody BaseRequest<String> request) {
        List<UserRoleResponse> menuTree = roleInfService.userRoles();
        return MsgUtil.ok(menuTree);
    }
}
