package cn.sinozg.applet.controller.device;

import cn.sinozg.applet.biz.device.service.DeviceGroupInfoService;
import cn.sinozg.applet.biz.device.vo.request.AddDeviceGroupRequest;
import cn.sinozg.applet.biz.device.vo.request.AssignGroupToUserRequest;
import cn.sinozg.applet.biz.device.vo.request.DeviceGroupInfoCreateRequest;
import cn.sinozg.applet.biz.device.vo.request.DeviceGroupInfoPageRequest;
import cn.sinozg.applet.biz.device.vo.request.DeviceGroupInfoUpdateRequest;
import cn.sinozg.applet.biz.device.vo.response.DeviceGroupInfoInfoResponse;
import cn.sinozg.applet.biz.device.vo.response.DeviceGroupInfoPageResponse;
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
 * @since 2023-12-03 16:27:01
 */
@RestController
@RequestMapping("/api/device/device_group_info")
@Tag(name = "device-group-info-controller", description = "设备分组信息表接口")
public class DeviceGroupInfoController {

    @Resource
    private DeviceGroupInfoService service;

    /**
    * 新增记录
    * @param request 请求参数
    * @return 是否成功
    */
    @PostMapping("/add")
    @Operation(summary = "新增设备分组信息表记录")
    public BaseResponse<String> addDeviceGroup(@RequestBody @Valid BaseRequest<DeviceGroupInfoCreateRequest> request) {
        DeviceGroupInfoCreateRequest params = MsgUtil.params(request);
        service.createInfo(params);
        return MsgUtil.ok();
    }

    /**
    * 删除记录
    * @param request 主键
    * @return 是否成功
    */
    @PostMapping("/delete")
    @Operation(summary = "根据主键删除设备分组信息表记录")
    public BaseResponse<String> deleteDeviceGroup(@RequestBody @Valid BaseRequest<ComId> request) {
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
    @Operation(summary = "查询设备分组信息表记录")
    public BaseResponse<DeviceGroupInfoInfoResponse> detailOfDeviceGroup(@RequestBody @Valid BaseRequest<ComId> request) {
        ComId params = MsgUtil.params(request);
        DeviceGroupInfoInfoResponse entity = service.getInfoById(params.getId());
        return MsgUtil.ok(entity);
    }

    /**
    * 更新记录
    * @param request 参数
    * @return 是否成功
    */
    @PostMapping("/update")
    @Operation(summary = "修改设备分组信息表记录")
    public BaseResponse<String> updateDeviceGroup(@RequestBody @Valid BaseRequest<DeviceGroupInfoUpdateRequest> request) {
        DeviceGroupInfoUpdateRequest params = MsgUtil.params(request);
        service.updateInfo(params);
        return MsgUtil.ok();
    }


    /**
    * 分页查询信息
    * @param request 查询参数
    * @return 分页记录
    */
    @PostMapping("/page_list")
    @Operation(summary = "查询设备分组信息表分页列表")
    public BasePageResponse<List<DeviceGroupInfoPageResponse>> pageOfDeviceGroup(@RequestBody @Valid BasePageRequest<DeviceGroupInfoPageRequest> request) {
        DeviceGroupInfoPageRequest params = MsgUtil.params(request);
        return service.pageInfo(request.getPage(), params);
    }

    /**
    * 添加设备到分组
    * @param request 查询参数
    * @return 分页记录
    */
    @PostMapping("/add_group_device")
    @Operation(summary = "添加设备到分组")
    public BaseResponse<String> addGroupDevice(@RequestBody @Valid BasePageRequest<AddDeviceGroupRequest> request) {
        AddDeviceGroupRequest params = MsgUtil.params(request);
        DeviceGroupInfoUpdateRequest param = new DeviceGroupInfoUpdateRequest();
        param.setId(params.getId());
        param.setGroupDeviceJson(params.getDeviceCodeList());
        service.updateInfo(param);
        return MsgUtil.ok();
    }

    /**
    * 分配分组到用户
    * @param request 查询参数
    * @return 分页记录
    */
    @PostMapping("/assign_group_to_user")
    @Operation(summary = "分配分组到用户")
    public BaseResponse<String> assignGroupToUser(@RequestBody @Valid BasePageRequest<AssignGroupToUserRequest> request) {
        AssignGroupToUserRequest params = MsgUtil.params(request);
        DeviceGroupInfoUpdateRequest param = new DeviceGroupInfoUpdateRequest();
        param.setId(params.getId());
        param.setGroupOwner(params.getGroupOwner());
        service.updateInfo(param);
        return MsgUtil.ok();
    }
}
