package cn.sinozg.applet.controller.device;

import cn.sinozg.applet.biz.device.service.DeviceInfoSendService;
import cn.sinozg.applet.biz.device.vo.request.DeviceOrderGetPropertyRequest;
import cn.sinozg.applet.biz.device.vo.request.DeviceOrderSetPropertyRequest;
import cn.sinozg.applet.common.core.base.BaseRequest;
import cn.sinozg.applet.common.core.base.BaseResponse;
import cn.sinozg.applet.common.utils.MsgUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-20 17:33
 */
@RestController
@RequestMapping("/api/device/order")
@Tag(name = "device-order-controller", description = "设备信息表接口")
public class DeviceOrderController {

    @Resource
    private DeviceInfoSendService deviceInfoSendService;

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
}
