package cn.sinozg.applet.controller.device;

import cn.sinozg.applet.biz.device.service.DeviceCategoryInfoService;
import cn.sinozg.applet.biz.device.vo.request.DeviceCategoryInfoCreateRequest;
import cn.sinozg.applet.biz.device.vo.request.DeviceCategoryInfoPageRequest;
import cn.sinozg.applet.biz.device.vo.request.DeviceCategoryInfoUpdateRequest;
import cn.sinozg.applet.biz.device.vo.response.DeviceCategoryInfoInfoResponse;
import cn.sinozg.applet.biz.device.vo.response.DeviceCategoryInfoPageResponse;
import cn.sinozg.applet.biz.protocol.vo.request.SelectByNameRequest;
import cn.sinozg.applet.biz.system.vo.response.DictListResponse;
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
 * @Description
 * @Copyright Copyright (c) 2023
 * @author wang.pf
 * @since 2023-12-03 16:48:09
 */
@RestController
@RequestMapping("/api/device/device_category_info")
@Tag(name = "device-category-info-controller", description = "设备分类信息表接口")
public class DeviceCategoryInfoController {

    @Resource
    private DeviceCategoryInfoService service;

    /**
    * 新增记录
    * @param request 请求参数
    * @return 是否成功
    */
    @PostMapping("/add")
    @Operation(summary = "新增设备分类信息表记录")
    public BaseResponse<String> addDeviceCategory(@RequestBody BaseRequest<DeviceCategoryInfoCreateRequest> request) {
        DeviceCategoryInfoCreateRequest params = MsgUtil.params(request);
        service.createInfo(params);
        return MsgUtil.ok();
    }

    /**
    * 删除记录
    * @param request 主键
    * @return 是否成功
    */
    @PostMapping("/delete")
    @Operation(summary = "根据主键删除设备分类信息表记录")
    public BaseResponse<String> deleteDeviceCategory(@RequestBody @Valid BaseRequest<ComId> request) {
        ComId params = MsgUtil.params(request);
        service.removeById(params.getId());
        return MsgUtil.ok();
    }

    /**
    * 查询详情
    * @param request 主键
    * @return 记录信息
    */
    @PostMapping("/detail")
    @Operation(summary = "查询设备分类信息表记录")
    public BaseResponse<DeviceCategoryInfoInfoResponse> detailOfDeviceCategory(@RequestBody @Valid BaseRequest<ComId> request) {
        ComId params = MsgUtil.params(request);
        DeviceCategoryInfoInfoResponse entity = service.getInfoById(params.getId());
        return MsgUtil.ok(entity);
    }

    /**
    * 更新记录
    * @param request 参数
    * @return 是否成功
    */
    @PostMapping("/update")
    @Operation(summary = "修改设备分类信息表记录")
    public BaseResponse<String> updateDeviceCategory(@RequestBody @Valid BaseRequest<DeviceCategoryInfoUpdateRequest> request) {
        DeviceCategoryInfoUpdateRequest params = MsgUtil.params(request);
        service.updateInfo(params);
        return MsgUtil.ok();
    }


    /**
    * 分页查询信息
    * @param request 查询参数
    * @return 分页记录
    */
    @PostMapping("/page_list")
    @Operation(summary = "查询设备分类信息表分页列表")
    public BasePageResponse<List<DeviceCategoryInfoPageResponse>> pageOfDeviceCategory(@RequestBody @Valid BasePageRequest<DeviceCategoryInfoPageRequest> request) {
        DeviceCategoryInfoPageRequest params = MsgUtil.params(request);
        return service.pageInfo(request.getPage(), params);
    }


    @PostMapping("/selectList")
    @Operation(summary = "查询所有的设备分类")
    public BaseResponse<List<DictListResponse>> selectList(@RequestBody @Valid BaseRequest<SelectByNameRequest> request) {
        SelectByNameRequest params = MsgUtil.params(request);
        List<DictListResponse> list = service.moduleList(params);
        return MsgUtil.ok(list);
    }

    /**
    * 通过父级编码查询下一级子分类
    * @param request 查询参数
    * @return 分页记录
    */
    @PostMapping("/find_sub_category")
    @Operation(summary = "通过父级编码查询下一级子分类")
    public BaseResponse<List<DeviceCategoryInfoInfoResponse>> findSubCategory(@RequestBody @Valid BaseRequest<ComId> request) {
        ComId comId = MsgUtil.params(request);
        return MsgUtil.ok(service.findSubCategory(comId.getId()));
    }
}
