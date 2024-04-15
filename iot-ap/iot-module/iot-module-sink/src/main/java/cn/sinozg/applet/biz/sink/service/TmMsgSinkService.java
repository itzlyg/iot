package cn.sinozg.applet.biz.sink.service;

import cn.sinozg.applet.biz.sink.vo.request.TmSinkAddRequest;
import cn.sinozg.applet.biz.sink.vo.request.TmSinkPageRequest;
import cn.sinozg.applet.biz.sink.vo.request.TmStaRequest;
import cn.sinozg.applet.biz.sink.vo.response.TmSinkPageResponse;
import cn.sinozg.applet.biz.sink.vo.response.TmStaResponse;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.PagingRequest;

import java.util.List;

/**
 * 物模型数据落地
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-12-06 19:02:23
 */
public interface TmMsgSinkService {

    /**
     * 按消息类型和标识符取设备消息
     * 分页查询
     * @param paging    分页
     * @param params   参数 分页
     */
    BasePageResponse<List<TmSinkPageResponse>> typeIdentifierPage(PagingRequest paging, TmSinkPageRequest params);

    /**
     * 按用户统计时间段内每小时上报次数
     * 做统计用 在首屏
     *
     * @param params   用户id 开始时间戳 结束时间戳
     */
    List<TmStaResponse> staDetail(TmStaRequest params);

    /**
     * 新增设备物模型数据入库到 时序数据库
     * @param msg 数据
     */
    void add(TmSinkAddRequest msg);

    /**
     * 统计总数
     * @return 总数
     */
    long count();
}
