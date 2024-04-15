package cn.sinozg.applet.controller.open;

import cn.sinozg.applet.biz.device.service.DeviceInfoSendService;
import cn.sinozg.applet.biz.device.service.DeviceInfoService;
import cn.sinozg.applet.biz.device.vo.request.DeviceInfoCreateRequest;
import cn.sinozg.applet.biz.device.vo.request.DeviceOrderGetPropertyRequest;
import cn.sinozg.applet.biz.device.vo.request.DeviceOrderSetPropertyRequest;
import cn.sinozg.applet.biz.device.vo.response.DeviceInfoInfoResponse;
import cn.sinozg.applet.biz.open.service.OutDeviceService;
import cn.sinozg.applet.biz.open.vo.request.FunctionExecuteRequest;
import cn.sinozg.applet.biz.open.vo.request.ReportCallbackRequest;
import cn.sinozg.applet.common.core.base.BaseRequest;
import cn.sinozg.applet.common.core.base.BaseResponse;
import cn.sinozg.applet.common.core.model.ComId;
import cn.sinozg.applet.common.utils.MsgUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 给外部提供接口 设备相关
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-03-07 20:05
 */
@Slf4j
@RestController
@RequestMapping("/api/open/device")
@Tag(name = "out-device-controller", description = "给外部提供设备相关的接口")
public class OutDeviceController {

    @Resource
    private DeviceInfoService service;

    @Resource
    private OutDeviceService outDeviceService;

    @Resource
    private DeviceInfoSendService deviceInfoSendService;


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
     * 设置设备属性
     * @param request 请求参数
     * @return 是否成功
     */
    @PostMapping("/reportProperty")
    @Operation(summary = "设置设备属性")
    public BaseResponse<String> setProperty(@RequestBody @Valid BaseRequest<DeviceOrderSetPropertyRequest> request) {
        DeviceOrderSetPropertyRequest params = MsgUtil.params(request);
        deviceInfoSendService.reportProperty(params.getDeviceId(), params.getSendName(), params.getProperties());
        return MsgUtil.ok();
    }

    @PostMapping("/getProperty")
    @Operation(summary = "获取设备属性")
    public BaseResponse<String> getProperty(@RequestBody @Valid BaseRequest<DeviceOrderGetPropertyRequest> request) {
        DeviceOrderGetPropertyRequest params = MsgUtil.params(request);
        String result = deviceInfoSendService.getProperty(params.getDeviceId(), params.getSendName(), params.getProperties());
        return MsgUtil.ok(result);
    }

    @PostMapping("/funExecute")
    @Operation(summary = "执行设备功能")
    public BaseResponse<String> funExecute(@RequestBody BaseRequest<FunctionExecuteRequest> request) {
        FunctionExecuteRequest params = MsgUtil.params(request);
        outDeviceService.functionExecute(params);
        return MsgUtil.ok();
    }

    @PostMapping("/callback")
    @Operation(summary = "回调接口的")
    public BaseResponse<String> callback(@RequestBody BaseRequest<ReportCallbackRequest> request) {
        ReportCallbackRequest params = MsgUtil.params(request);
        return MsgUtil.ok();
    }
}
