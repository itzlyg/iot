package cn.sinozg.applet.biz.sink.service.impl;

import cn.sinozg.applet.biz.com.enums.TmIdentifierType;
import cn.sinozg.applet.biz.sink.entity.ThingModelMessage;
import cn.sinozg.applet.biz.sink.service.ThingModelMessageService;
import cn.sinozg.applet.biz.sink.service.TmMsgSinkService;
import cn.sinozg.applet.biz.sink.vo.request.TmSinkAddRequest;
import cn.sinozg.applet.biz.sink.vo.request.TmSinkPageRequest;
import cn.sinozg.applet.biz.sink.vo.request.TmStaRequest;
import cn.sinozg.applet.biz.sink.vo.response.TmSinkPageResponse;
import cn.sinozg.applet.biz.sink.vo.response.TmStaResponse;
import cn.sinozg.applet.common.constant.RedisKey;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.PagingRequest;
import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-06 19:02
 */
@Slf4j
@Service
public class TmMsgSinkServiceImpl implements TmMsgSinkService {

    @Resource
    private ThingModelMessageService thingModelMessageService;

    @Override
    public BasePageResponse<List<TmSinkPageResponse>> typeIdentifierPage(PagingRequest paging, TmSinkPageRequest params) {
        BasePageResponse<List<TmSinkPageResponse>> response = thingModelMessageService.typeIdentifierPage(paging, params);
        if (CollectionUtils.isNotEmpty(response.getData())) {
            response.getData().forEach(tm -> {
                tm.setData(JsonUtil.toMap(PojoUtil.cast(tm.getData())));
                tm.setIdentifier(tmIdentifierType(tm.getIdentifier()));
            });
        }

        return response;
    }

    @Override
    public List<TmStaResponse> staDetail(TmStaRequest params) {
        return thingModelMessageService.staDetail(params);
    }

    @Override
    public void add(TmSinkAddRequest params) {
        ThingModelMessage modelMessage = new ThingModelMessage();
        modelMessage.setProdKey(params.getProdKey());
        modelMessage.setDeviceCode(params.getDeviceCode());
        modelMessage.setDeviceName(params.getDeviceName());
        String mid = params.getMid();
        String midName = RedisUtil.getCacheObject(String.format(RedisKey.MID_NAME, mid));
        modelMessage.setMid(mid);

        modelMessage.setMidName(midName);
        modelMessage.setUid(params.getUid());
        modelMessage.setType(params.getType());
        modelMessage.setIdentifier(params.getIdentifier());
        modelMessage.setOrderTp(params.getOrderTp());
        modelMessage.setTs(params.getOccurred());

        modelMessage.setCode(params.getCode());
        modelMessage.setReportTime(params.getTs());
        modelMessage.setData(params.getSaveParams());

        thingModelMessageService.save(modelMessage);
    }

    @Override
    public long count() {
        return thingModelMessageService.count();
    }

    /**
     * 转化标志符号
     * @param enName 标志符号
     * @return 标志符号名称
     */
    private String tmIdentifierType(String enName){
        TmIdentifierType tp;
        boolean isReply = TmIdentifierType.isReply(enName);
        if (!isReply) {
            tp = TmIdentifierType.ofEnName(enName);
        } else {
            tp = TmIdentifierType.ofEnName(StringUtils.replace(enName, TmIdentifierType.REPLY.getEnName(), StringUtils.EMPTY));
        }
        if (tp != null) {
            return tp.getName() + (isReply ? "(" + TmIdentifierType.REPLY.getName() + ")" : StringUtils.EMPTY);
        }
        return null;
    }

}
