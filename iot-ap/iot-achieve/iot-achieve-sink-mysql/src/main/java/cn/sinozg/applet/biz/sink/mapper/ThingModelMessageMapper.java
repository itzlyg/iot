package cn.sinozg.applet.biz.sink.mapper;

import cn.sinozg.applet.biz.com.vo.response.OptimizeTableResponse;
import cn.sinozg.applet.biz.sink.entity.ThingModelMessage;
import cn.sinozg.applet.biz.sink.vo.request.TmSinkPageRequest;
import cn.sinozg.applet.biz.sink.vo.request.TmStaRequest;
import cn.sinozg.applet.biz.sink.vo.response.TmSinkPageResponse;
import cn.sinozg.applet.biz.sink.vo.response.TmStaResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 物模型数据记录表 Mapper 接口
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-03-07 16:13:16
*/
public interface ThingModelMessageMapper extends BaseMapper<ThingModelMessage> {

    /**
     * 删除日志
     */
    long deleteLog(@Param("ymd") String ymd);

    /**
     * 优化表
     */
    List<OptimizeTableResponse> optimizeTable();

    List<TmStaResponse> staDetail(@Param("p") TmStaRequest params);

    IPage<TmSinkPageResponse> typeIdentifierPage(Page<TmSinkPageResponse> page, @Param("p") TmSinkPageRequest params);
}
