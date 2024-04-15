package cn.sinozg.applet.controller.device;

import cn.sinozg.applet.biz.device.service.DeviceInfoService;
import cn.sinozg.applet.biz.device.vo.request.DeviceInfoCreateRequest;
import cn.sinozg.applet.biz.device.vo.request.DeviceInfoPageRequest;
import cn.sinozg.applet.biz.device.vo.request.DeviceInfoUpdateRequest;
import cn.sinozg.applet.biz.device.vo.response.DeviceInfoInfoResponse;
import cn.sinozg.applet.biz.device.vo.response.DeviceInfoPageResponse;
import cn.sinozg.applet.common.core.base.BasePageRequest;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.BaseRequest;
import cn.sinozg.applet.common.core.base.BaseResponse;
import cn.sinozg.applet.common.core.model.ComId;
import cn.sinozg.applet.common.utils.MsgUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/api/device/device_info")
@Tag(name = "device-info-controller", description = "设备信息表接口")
public class DeviceInfoController {

    @Resource
    private DeviceInfoService service;

    /**
    * 新增记录
    * @param request 请求参数
    * @return 是否成功
    */
    @PostMapping("/add")
    @Operation(summary = "新增设备信息表记录")
    public BaseResponse<String> addDevice(@RequestBody @Valid BaseRequest<DeviceInfoCreateRequest> request) {
        DeviceInfoCreateRequest params = MsgUtil.params(request);
        service.createInfo(params);
        return MsgUtil.ok();
    }

    /**
    * 删除记录
    * @param request 主键
    * @return 是否成功
    */
    @PostMapping("/delete")
    @Operation(summary = "根据主键删除设备信息表记录")
    public BaseResponse<String> deleteDevice(@RequestBody @Valid BaseRequest<ComId> request) {
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
    @Operation(summary = "查询设备信息表记录")
    public BaseResponse<DeviceInfoInfoResponse> detailOfDevice(@RequestBody @Valid BaseRequest<ComId> request) {
        ComId params = MsgUtil.params(request);
        DeviceInfoInfoResponse entity = service.getInfoById(params.getId());
        return MsgUtil.ok(entity);
    }

    /**
    * 更新记录
    * @param request 参数
    * @return 是否成功
    */
    @PostMapping("/update")
    @Operation(summary = "修改设备信息表记录")
    public BaseResponse<String> updateDevice(@RequestBody @Valid BaseRequest<DeviceInfoUpdateRequest> request) {
        DeviceInfoUpdateRequest params = MsgUtil.params(request);
        service.updateInfo(params);
        return MsgUtil.ok();
    }


    /**
    * 分页查询信息
    * @param request 查询参数
    * @return 分页记录
    */
    @PostMapping("/page_list")
    @Operation(summary = "查询设备信息表分页列表")
    public BasePageResponse<List<DeviceInfoPageResponse>> pageOfDevice(@RequestBody @Valid BasePageRequest<DeviceInfoPageRequest> request) {
        DeviceInfoPageRequest params = MsgUtil.params(request);
        return service.pageInfo(request.getPage(), params);
    }

}
