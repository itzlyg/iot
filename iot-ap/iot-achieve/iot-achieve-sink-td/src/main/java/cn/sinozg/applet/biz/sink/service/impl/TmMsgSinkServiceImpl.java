package cn.sinozg.applet.biz.sink.service.impl;

import cn.sinozg.applet.biz.com.enums.TmIdentifierType;
import cn.sinozg.applet.biz.sink.common.TdContext;
import cn.sinozg.applet.biz.sink.common.TdSql;
import cn.sinozg.applet.biz.sink.service.TmMsgSinkService;
import cn.sinozg.applet.biz.sink.util.TdTableUtil;
import cn.sinozg.applet.biz.sink.vo.request.TmSinkAddRequest;
import cn.sinozg.applet.biz.sink.vo.request.TmSinkPageRequest;
import cn.sinozg.applet.biz.sink.vo.request.TmStaRequest;
import cn.sinozg.applet.biz.sink.vo.response.TmSinkPageResponse;
import cn.sinozg.applet.biz.sink.vo.response.TmStaResponse;
import cn.sinozg.applet.common.constant.RedisKey;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.PagingRequest;
import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.common.utils.PageConvert;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    @Resource(name = TdContext.JDBC_TEMPLATE)
    private JdbcTemplate template;
    @Override
    public BasePageResponse<List<TmSinkPageResponse>> typeIdentifierPage(PagingRequest paging, TmSinkPageRequest params) {
        List<Object> args = new ArrayList<>();
        args.add(params.getDeviceCode());
        StringBuilder where = new StringBuilder();
        if (StringUtils.isNotBlank(params.getType())) {
            where.append(" and type=? ");
            args.add(params.getType());
        }
        if (StringUtils.isNotBlank(params.getIdentifier())) {
            where.append("and identifier=? ");
            args.add(params.getIdentifier());
        }
        String sql = String.format(TdSql.TH_PAGE_COUNT, where);
        List<Long> counts = template.queryForList(sql, Long.class, args.toArray());
        long count = TdTableUtil.count(counts);
        List<TmSinkPageResponse> list = null;
        if (count != 0) {
            sql = String.format(TdSql.TH_PAGE_DETAIL, where, paging.getPageSize(), (paging.getPageNum() - 1) * paging.getPageSize());
            list = template.query(sql, new BeanPropertyRowMapper<>(TmSinkPageResponse.class), args.toArray());
            if (CollectionUtils.isNotEmpty(list)) {
                list.forEach(tm -> {
                    tm.setData(JsonUtil.toMap(PojoUtil.cast(tm.getData())));
                    tm.setIdentifier(tmIdentifierType(tm.getIdentifier()));
                });
            }
        }
        return PageConvert.convert(paging, list, count);
    }

    @Override
    public List<TmStaResponse> staDetail(TmStaRequest params) {
        String where = StringUtils.EMPTY;
        List<Object> args = new ArrayList<>();
        args.add(params.getStart());
        args.add(params.getEnd());
        if (StringUtils.isNotBlank(params.getUid())) {
            args.add(params.getUid());
            where = " and uid = ? ";
        }
        return template.query(String.format(TdSql.TH_STA, where), new BeanPropertyRowMapper<>(TmStaResponse.class), args.toArray());
    }

    @Override
    public void add(TmSinkAddRequest params) {
        String sql = String.format(TdSql.TH_ADD, params.getDeviceCode().toLowerCase(), params.getDeviceCode());
        String data = params.getSaveParams();
        String mid = params.getMid();
        String midName = RedisUtil.getCacheObject(String.format(RedisKey.MID_NAME, mid));
        template.update(sql, params.getOccurred(), mid, midName, params.getProdKey(), params.getDeviceName(),
                params.getUid(), params.getType(), params.getIdentifier(), params.getOrderTp(), params.getCode(),
                data, params.getTs());
    }

    @Override
    public long count() {
        List<Long> counts = template.queryForList(TdSql.TH_COUNT, Long.class);
        return TdTableUtil.count(counts);
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
