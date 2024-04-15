package cn.sinozg.applet.biz.sink.service.impl;

import cn.sinozg.applet.biz.sink.entity.ThingModelMessage;
import cn.sinozg.applet.biz.sink.mapper.ThingModelMessageMapper;
import cn.sinozg.applet.biz.sink.service.ThingModelMessageService;
import cn.sinozg.applet.biz.sink.vo.request.TmSinkPageRequest;
import cn.sinozg.applet.biz.sink.vo.request.TmStaRequest;
import cn.sinozg.applet.biz.sink.vo.response.TmSinkPageResponse;
import cn.sinozg.applet.biz.sink.vo.response.TmStaResponse;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.PagingRequest;
import cn.sinozg.applet.common.utils.PageUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* 物模型数据记录表 服务实现类
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-03-07 16:13:16
*/
@Service
public class ThingModelMessageServiceImpl extends ServiceImpl<ThingModelMessageMapper, ThingModelMessage> implements ThingModelMessageService {

    @Resource
    private ThingModelMessageMapper mapper;

    @Override
    public List<TmStaResponse> staDetail(TmStaRequest params) {
        return mapper.staDetail(params);
    }

    @Override
    public BasePageResponse<List<TmSinkPageResponse>> typeIdentifierPage (PagingRequest paging, TmSinkPageRequest params){
        PageUtil<TmSinkPageResponse, TmSinkPageRequest> pu = (p, q) -> mapper.typeIdentifierPage(p, q);
        return pu.page(paging, params);
    }
}
