package cn.sinozg.applet.biz.sink.service;

import cn.sinozg.applet.biz.sink.entity.ThingModelMessage;
import cn.sinozg.applet.biz.sink.vo.request.TmSinkPageRequest;
import cn.sinozg.applet.biz.sink.vo.request.TmStaRequest;
import cn.sinozg.applet.biz.sink.vo.response.TmSinkPageResponse;
import cn.sinozg.applet.biz.sink.vo.response.TmStaResponse;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.PagingRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* 物模型数据记录表 服务类
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-03-07 16:13:16
*/
public interface ThingModelMessageService extends IService<ThingModelMessage> {

    /**
     * 清理日志
     */
    void clearLog ();

    List<TmStaResponse> staDetail(TmStaRequest params);

    BasePageResponse<List<TmSinkPageResponse>> typeIdentifierPage (PagingRequest paging, TmSinkPageRequest params);
}
