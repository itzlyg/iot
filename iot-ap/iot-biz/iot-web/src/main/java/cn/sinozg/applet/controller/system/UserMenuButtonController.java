package cn.sinozg.applet.controller.system;

import cn.sinozg.applet.biz.system.service.MenuInfoService;
import cn.sinozg.applet.biz.system.vo.request.MenuAddRequest;
import cn.sinozg.applet.biz.system.vo.request.MenuInfoEditRequest;
import cn.sinozg.applet.biz.system.vo.request.MenuLazyPageRequest;
import cn.sinozg.applet.biz.system.vo.request.MenuTreeRequest;
import cn.sinozg.applet.biz.system.vo.request.MenuUserQueryRequest;
import cn.sinozg.applet.biz.system.vo.response.MenuDetailResponse;
import cn.sinozg.applet.biz.system.vo.response.MenuLazyPageResponse;
import cn.sinozg.applet.biz.system.vo.response.UserSystemMenuResponse;
import cn.sinozg.applet.common.core.base.BaseRequest;
import cn.sinozg.applet.common.core.base.BaseResponse;
import cn.sinozg.applet.common.core.model.ComId;
import cn.sinozg.applet.common.core.model.TreeSelect;
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
* 用户菜单按钮相关的
* @Author: xyb
* @Description:
* @Date: 2023-04-11 下午 08:48
**/
@RestController
@RequestMapping("/api/menu/info")
@Tag(name = "user-menu-button-controller", description = "用户菜单按钮信息表接口")
public class UserMenuButtonController {

    @Resource
    private MenuInfoService menuInfService;

    /**
     * 获取用户菜单信息
     * 用于菜单树 层级打开
     *
     * @param request 查询参数
     * @return
     */
    @PostMapping("/lazy_page")
    @Operation(summary = "获取用户菜单信息 懒加载，用于菜单树展示 类似分页")
    public BaseResponse<List<MenuLazyPageResponse>> menuLazyPage(@RequestBody @Valid BaseRequest<MenuLazyPageRequest> request) {
        MenuLazyPageRequest params = MsgUtil.params(request);
        List<MenuLazyPageResponse> list = menuInfService.menuLazyPage(params);
        return MsgUtil.ok(list);
    }

    /**
     * 菜单树点开被选择
     * @param request
     * @return
     */
    @PostMapping("/menu_tree")
    @Operation(summary = "获取用户菜单信息")
    public BaseResponse<List<TreeSelect>> menuTree(@RequestBody @Valid BaseRequest<MenuTreeRequest> request) {
        MenuTreeRequest params = MsgUtil.params(request);
        List<TreeSelect> list = menuInfService.menuTree(params);
        return MsgUtil.ok(list);
    }

    @PostMapping(value = "/add")
    @Operation(summary = "添加用户菜单信息", hidden = true)
    public BaseResponse<String> addMenu(@RequestBody @Valid BaseRequest<MenuAddRequest> request) {
        MenuAddRequest params = MsgUtil.params(request);
        menuInfService.addMenu(params);
        return MsgUtil.ok();
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @PostMapping(value = "/detail")
    @Operation(summary = "菜单详情")
    public BaseResponse<MenuDetailResponse> getMenuInfo(@RequestBody @Valid BaseRequest<ComId> request) {
        ComId comId = MsgUtil.params(request);
        MenuDetailResponse response = menuInfService.selectMenuById(comId.getId());
        return MsgUtil.ok(response);
    }

    /**
     * 修改菜单
     */
    @PostMapping("/edit")
    @Operation(summary = "修改菜单")
    public BaseResponse<String> editMenu(@RequestBody @Valid BaseRequest<MenuInfoEditRequest> request) {
        MenuInfoEditRequest menu = MsgUtil.params(request);
        menuInfService.editMenu(menu);
        return MsgUtil.ok();
    }

    /**
     * 删除菜单
     */
    @PostMapping("/delete")
    @Operation(summary = "删除菜单")
    public BaseResponse<String> deleteMenu(@RequestBody @Valid BaseRequest<ComId> request) {
        ComId comId = MsgUtil.params(request);
        menuInfService.deleteMenuById(comId.getId());
        return MsgUtil.ok();
    }

    @PostMapping("/routers")
    @Operation(summary = "获取用户的获取路由信息 用户菜单树，用于展示 系统菜单")
    public BaseResponse<List<UserSystemMenuResponse>> userMenu (@RequestBody @Valid BaseRequest<MenuUserQueryRequest> request){
        MenuUserQueryRequest params = MsgUtil.params(request);
        List<UserSystemMenuResponse> response = menuInfService.userSystemMenu(params.getChannel());
        return MsgUtil.ok(response);
    }

    @PostMapping("/button")
    @Operation(summary = "获取用户的按钮id信息", hidden = true)
    public BaseResponse<List<String>> button (@RequestBody @Valid BaseRequest<MenuUserQueryRequest> request){
        MenuUserQueryRequest params = MsgUtil.params(request);
        List<String> response = menuInfService.userSystemButton(params.getChannel());
        return MsgUtil.ok(response);
    }
}
