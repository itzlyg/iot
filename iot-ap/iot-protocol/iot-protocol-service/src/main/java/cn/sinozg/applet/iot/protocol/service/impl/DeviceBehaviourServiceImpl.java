package cn.sinozg.applet.iot.protocol.service.impl;

import cn.sinozg.applet.biz.com.enums.TmIdentifierType;
import cn.sinozg.applet.biz.com.enums.TmType;
import cn.sinozg.applet.biz.com.model.TmMessageInfo;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.common.utils.SnowFlake;
import cn.sinozg.applet.iot.common.enums.ProtocolTopicType;
import cn.sinozg.applet.iot.common.utils.ProtocolUtil;
import cn.sinozg.applet.iot.protocol.model.DeviceAuthInfo;
import cn.sinozg.applet.iot.protocol.model.DeviceRegisterInfo;
import cn.sinozg.applet.iot.protocol.model.DeviceSubRegisterInfo;
import cn.sinozg.applet.iot.protocol.service.DeviceProtocolDataService;
import cn.sinozg.applet.iot.protocol.vo.response.DeviceInfoProtocolResponse;
import cn.sinozg.applet.iot.protocol.vo.response.DeviceOtaDetailResponse;
import cn.sinozg.applet.iot.protocol.vo.response.ProductModelProtocolResponse;
import cn.sinozg.applet.iot.protocol.vo.response.ProductProtocolResponse;
import cn.sinozg.applet.mq.mq.MqProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-11-15 22:40:18
 */
@Slf4j
@Service
public class DeviceBehaviourServiceImpl {

    @Resource
    private DeviceProtocolDataService dataService;
    @Resource
    private MqProducer<TmMessageInfo> producer;

    public void register(DeviceRegisterInfo info) {
        try {
            DeviceInfoProtocolResponse deviceInfo = register(null, info);
            //子设备注册
            List<DeviceSubRegisterInfo> subDevices = info.getSubDevices();
            if (subDevices == null) {
                return;
            }
            for (DeviceSubRegisterInfo subDevice : subDevices) {
                DeviceRegisterInfo registerInfo = new DeviceRegisterInfo();
                registerInfo.setProdKey(subDevice.getProdKey());
                registerInfo.setDeviceCode(subDevice.getDeviceCode());
                registerInfo.setTag(subDevice.getTag());
                registerInfo.setModel(subDevice.getModel());
                register(deviceInfo.getDeviceCode(), registerInfo);
            }
        } catch (Exception e) {
            log.error("register device error", e);
            throw new CavException("注册设备失败！", e);
        }
    }

    public DeviceInfoProtocolResponse register(String parentId, DeviceRegisterInfo info) {
        String pk = info.getProdKey();
        String deviceCode = info.getDeviceCode();
        String model = info.getModel();
        //子设备注册处理
        if (parentId != null) {
            //透传设备：pk为空、model不为空，使用model查询产品
            if (StringUtils.isBlank(pk) && StringUtils.isNotBlank(model)) {
                ProductModelProtocolResponse productModel = dataService.modelInfoByModel(model);
                if (productModel == null) {
                    throw new CavException("未找到产品模型！");
                }
                pk = productModel.getProdKey();
            }
        }

        ProductProtocolResponse product = dataService.productInfoByKey(pk);
        if (product == null) {
            throw new CavException("未找到产品！");
        }
        String uid = product.getUid();
        DeviceInfoProtocolResponse device = dataService.deviceInfoKeyCode(pk, deviceCode, false);
        boolean reportMsg = false;

        if (device != null) {
            log.info("device already registered");
            device.setModel(model);
        } else {
            //不存在,注册新设备
            device = new DeviceInfoProtocolResponse();
            device.setParentId(parentId);
            device.setUid(uid);
            device.setDeviceCode(device.getId());
            device.setProdKey(pk);
            device.setModel(model);
            //默认离线
            device.setOnline(false);
            reportMsg = true;
            //auth、acl
        }

        // 透传设备，默认在线
        if (product.isTransparent()) {
            device.setOnline(true);
        }

        if (parentId != null) {
            //子设备更换网关重新注册更新父级ID
            device.setParentId(parentId);
            reportMsg = true;
        }
        dataService.saveDevice(device);

        //新设备或更换网关需要产生注册消息
        if (reportMsg) {
            log.info("device registered:{}", JsonUtil.toJson(device));
            // 新注册设备注册消息
            TmMessageInfo messageInfo = new TmMessageInfo();
            messageInfo.setMid(ProtocolUtil.requestId());
            messageInfo.setDeviceCode(StringUtils.EMPTY);
            messageInfo.setProdKey(pk);
            messageInfo.setUid(uid);

            messageInfo.setType(TmType.LIFE_TIME);
            messageInfo.setIdentifier(TmIdentifierType.REGISTER);
            messageInfo.setCode(0);
            messageInfo.setData(new HashMap<>(16));
            messageInfo.setTime(System.currentTimeMillis());

            messageInfo.setOccurred(System.currentTimeMillis());

            reportMessage(messageInfo);
        }

        return device;
    }

    public void deviceAuth(DeviceAuthInfo authInfo) {
        String prodKey = authInfo.getProdKey();
        DeviceInfoProtocolResponse deviceInfo = dataService.deviceInfoKeyCode(prodKey, authInfo.getDeviceCode(), false);
        if (deviceInfo == null) {
            throw new CavException("设备未找到！");
        }
        ProductProtocolResponse product = dataService.productInfoByKey(prodKey);
        if (!product.getProdSecret().equals(authInfo.getProdSecret())) {
            throw new CavException("产品密钥错误！");
        }

        //todo 按产品ProdSecret认证，子设备需要父设备认证后可通过验证
//        Optional<Product> optProduct = productRepository.findById(prodKey);
//        if (!optProduct.isPresent()) {
//            throw new BizException("product does not exist");
//        }
//        Product product = optProduct.get();
//        if (product.getNodeType()) {
//
//        }

    }

    public boolean isOnline(String prodKey, String deviceCode) {
        DeviceInfoProtocolResponse device = dataService.deviceInfoKeyCode(prodKey, deviceCode);
        return device.getOnline();
    }

    public void deviceStateChange(String prodKey, String deviceCode, boolean online) {
        DeviceInfoProtocolResponse device = dataService.deviceInfoKeyCode(prodKey, deviceCode, false);
        if (device == null) {
            log.warn("prodKey: {},deviceCode:{},online: {}", prodKey, deviceCode, online);
            throw new CavException("设备未找到！");
        }
        deviceStateChange(device, online);
        //父设备ID不为空说明是子设备
        if (device.getParentId() != null) {
            return;
        }

        //否则为父设备，同步透传子设备状态
        List<String> subList = dataService.findSubDeviceCodes(device.getDeviceCode());
        for (String code : subList) {
            DeviceInfoProtocolResponse subDevice = dataService.deviceInfoByCode(code);
            ProductProtocolResponse product = dataService.productInfoByKey(subDevice.getProdKey());
            //透传设备父设备上线，子设备也上线。非透传设备父设备离线，子设备才离线
            if (product.isTransparent() || !online) {
                deviceStateChange(subDevice, online);
            }
        }
    }

    private void deviceStateChange(DeviceInfoProtocolResponse device, boolean online) {
        device.setOnline(online);
        dataService.saveDevice(device);

        // 设备状态变更消息
        TmMessageInfo messageInfo = new TmMessageInfo();
        messageInfo.setMid(ProtocolUtil.requestId());
        messageInfo.setDeviceCode(StringUtils.EMPTY);
        messageInfo.setProdKey(device.getProdKey());
        messageInfo.setDeviceName(device.getDeviceName());
        messageInfo.setUid(device.getUid());

        messageInfo.setType(TmType.STATE);
        messageInfo.setIdentifier(online ? TmIdentifierType.ONLINE : TmIdentifierType.OFFLINE);
        messageInfo.setCode(0);
        messageInfo.setData(new HashMap<>(16));
        messageInfo.setTime(System.currentTimeMillis());

        messageInfo.setOccurred(System.currentTimeMillis());


        reportMessage(messageInfo);
    }

    public void reportMessage(TmMessageInfo message) {
        try {
            DeviceInfoProtocolResponse device = dataService.deviceInfoKeyCode(message.getProdKey(), message.getDeviceCode(), false);
            if (device == null) {
                return;
            }
            message.setId(SnowFlake.genId());
            if (message.getOccurred() == null) {
                message.setOccurred(System.currentTimeMillis());
            }
            if (message.getTime() == null) {
                message.setTime(System.currentTimeMillis());
            }
            message.setDeviceCode(device.getDeviceCode());
            ProtocolUtil.setTenantId(message);
            producer.publish(ProtocolTopicType.THING_MODEL, message);
        } catch (Throwable e) {
            log.error("send thing model message error", e);
        }
    }

    /**
     * 提供给js调用的方法
     */
    public void reportMessage(String jsonMsg) {
        TmMessageInfo message = JsonUtil.toPojo(jsonMsg, TmMessageInfo.class);
        reportMessage(message);
    }

    public void deviceOta(TmMessageInfo message) {
        Object data = message.getData();
        if (data == null || (data instanceof String)) {
            log.error("ota数据格式错误 deviceCode:{}", message.getDeviceCode());
            return;
        }
        DeviceOtaDetailResponse otaDetail = JsonUtil.toPojo(PojoUtil.cast(data), DeviceOtaDetailResponse.class);
        if (otaDetail == null) {
            log.debug("device ota upload data is null deviceCode:{}", message.getDeviceCode());
            return;
        }
        otaDetail.setTaskId(message.getMid());
        otaDetail.setDeviceCode(message.getDeviceCode());
        otaDetail.setDeviceName(message.getDeviceName());
        otaDetail.setProdKey(message.getProdKey());

        DeviceOtaDetailResponse query = new DeviceOtaDetailResponse();
        query.setTaskId(message.getMid());
        query.setProdKey(message.getProdKey());
        query.setDeviceName(message.getDeviceName());
        query.setDeviceCode(message.getDeviceCode());

        DeviceOtaDetailResponse deviceOtaDetail = dataService.deviceOtaDetail(query);
        if (deviceOtaDetail != null) {
            deviceOtaDetail.setStep(otaDetail.getStep());
        } else {
            deviceOtaDetail = otaDetail;
        }
        dataService.saveDeviceOtaDetail(deviceOtaDetail);
    }
}
