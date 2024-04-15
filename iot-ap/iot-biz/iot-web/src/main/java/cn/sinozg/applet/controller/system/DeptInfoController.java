package cn.sinozg.applet.controller.system;

import cn.sinozg.applet.biz.system.service.DeptInfoService;
import cn.sinozg.applet.biz.system.vo.request.DeptInfoCreateRequest;
import cn.sinozg.applet.biz.system.vo.request.DeptInfoPageRequest;
import cn.sinozg.applet.biz.system.vo.request.DeptInfoUpdateRequest;
import cn.sinozg.applet.biz.system.vo.request.DeptTreeIdRequest;
import cn.sinozg.applet.biz.system.vo.response.DeptInfoDetailResponse;
import cn.sinozg.applet.biz.system.vo.response.DeptInfoPageResponse;
import cn.sinozg.applet.common.core.base.BasePageRequest;
import cn.sinozg.applet.common.core.base.BasePageResponse;
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
 * 前端控制器
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-09-06 11:22:14
 */
@RestController
@RequestMapping("/api/system/dept")
@Tag(name = "dept-info-controller", description = "部门信息接口")
public class DeptInfoController {

    @Resource
    private DeptInfoService service;

    /**
    * 新增记录
    * @param request 请求参数
    * @return 是否成功
    */
    @PostMapping("/add")
    @Operation(summary = "新增部门信息记录")
    public BaseResponse<String> addDept(@RequestBody @Valid BaseRequest<DeptInfoCreateRequest> request) {
        DeptInfoCreateRequest params = MsgUtil.params(request);
        service.createInfo(params);
        return MsgUtil.ok();
    }

    /**
    * 删除记录
    * @param request 主键
    * @return 是否成功
    */
    @PostMapping("/delete")
    @Operation(summary = "根据主键删除部门信息记录")
    public BaseResponse<String> deleteDept(@RequestBody @Valid BaseRequest<ComId> request) {
        ComId params = MsgUtil.params(request);
        String paterId = service.deleteDept(params.getId());
        return MsgUtil.ok(paterId);
    }


    /**
    * 更新记录
    * @param request 参数
    * @return 是否成功
    */
    @PostMapping("/update")
    @Operation(summary = "修改部门信息记录")
    public BaseResponse<String> updateDept(@RequestBody @Valid BaseRequest<DeptInfoUpdateRequest> request) {
        DeptInfoUpdateRequest params = MsgUtil.params(request);
        service.updateInfo(params);
        return MsgUtil.ok();
    }

    /**
     * 部门详情
     * @param request 参数
     * @return 部门详情
     */
    @PostMapping("/detail")
    @Operation(summary = "获取部门详情")
    public BaseResponse<DeptInfoDetailResponse> detail(@RequestBody @Valid BaseRequest<ComId> request) {
        ComId params = MsgUtil.params(request);
        DeptInfoDetailResponse detail = service.detail(params.getId());
        return MsgUtil.ok(detail);
    }

    /**
    * 部门树
    * @param request 查询参数
    * @return 部门树
    */
    @PostMapping("/tree")
    @Operation(summary = "部门树 用于下拉选择")
    public BaseResponse<List<TreeSelect>> deptTree(@RequestBody @Valid BaseRequest<DeptTreeIdRequest> request) {
        DeptTreeIdRequest params = MsgUtil.params(request);
        List<TreeSelect> tree = service.listByPaterId(params.getPaterId(), true);
        return MsgUtil.ok(tree);
    }

    @PostMapping("/pageTree")
    @Operation(summary = "部门树分页列表")
    public BasePageResponse<List<DeptInfoPageResponse>> pageTree(@RequestBody @Valid BasePageRequest<DeptInfoPageRequest> request) {
        return service.deptPageInfo(request);
    }

    @PostMapping("/treeSub")
    @Operation(summary = "部门树分页列表 懒加载，查询子列表信息")
    public BaseResponse<List<DeptInfoPageResponse>> treeSub(@RequestBody @Valid BaseRequest<ComId> request) {
        ComId params = MsgUtil.params(request);
        List<DeptInfoPageResponse> list = service.deptSubInfo(params.getId());
        return MsgUtil.ok(list);
    }
}
