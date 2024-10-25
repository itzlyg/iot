package cn.sinozg.applet.biz.open.service.impl;

import cn.sinozg.applet.biz.device.entity.DeviceInfo;
import cn.sinozg.applet.biz.device.service.DeviceInfoSendService;
import cn.sinozg.applet.biz.device.service.DeviceInfoService;
import cn.sinozg.applet.biz.device.vo.response.device.FunctionElement;
import cn.sinozg.applet.biz.open.service.OutDeviceService;
import cn.sinozg.applet.biz.open.vo.request.FunctionExecuteRequest;
import cn.sinozg.applet.biz.task.vo.request.param.TaskFunctionParamRequest;
import cn.sinozg.applet.common.exception.CavException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-03-07 20:08
 */
@Slf4j
@Service
public class OutDeviceServiceImpl implements OutDeviceService {

    @Resource
    private DeviceInfoService deviceInfoService;
    @Resource
    private DeviceInfoSendService deviceInfoSendService;

    @Override
    public void functionExecute(FunctionExecuteRequest request) {
        // 查询功能
        ImmutablePair<DeviceInfo, FunctionElement> pair = deviceInfoService.functionInfo(request.getProdKey(), request.getDeviceCode(), request.getFunctionId());
        if (pair == null) {
            throw new CavException("设备功能不存在！");
        }
        FunctionElement fun = pair.getValue();
        DeviceInfo deviceInfo = pair.getKey();
        Map<String, Object> paramsMap = new HashMap<>(64);
        for (TaskFunctionParamRequest p : request.getArgs()) {
            paramsMap.put(p.getParameterIdentifier(), p.getValue());
        }
        String mid = deviceInfoSendService.invokeService(deviceInfo.getId(), fun.getName(), request.getFunctionId(), paramsMap);
        log.info("外部调用方法，返回的请求id {}，参数:{}", mid, request);
    }
}
