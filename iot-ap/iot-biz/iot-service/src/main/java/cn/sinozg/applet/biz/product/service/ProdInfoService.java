package cn.sinozg.applet.biz.product.service;

import cn.sinozg.applet.biz.com.vo.response.TmProtocolResponse;
import cn.sinozg.applet.biz.product.entity.ProdInfo;
import cn.sinozg.applet.biz.product.vo.request.ProdInfoCreateRequest;
import cn.sinozg.applet.biz.product.vo.request.ProdInfoPageRequest;
import cn.sinozg.applet.biz.product.vo.request.ProdInfoUpdateRequest;
import cn.sinozg.applet.biz.product.vo.response.ProdInfoInfoResponse;
import cn.sinozg.applet.biz.product.vo.response.ProdInfoPageResponse;
import cn.sinozg.applet.biz.product.vo.response.ProdListResponse;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.PagingRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* 产品消息表 服务类
* @Description
* @Copyright Copyright (c) 2023
* @author zy
* @since 2023-11-27 21:44:29
*/
public interface ProdInfoService extends IService<ProdInfo> {

    /**
    * 新增产品消息表
    * @param params 产品消息表
    */
    void createInfo (ProdInfoCreateRequest params);

    /**
    * 查询详情
    * @param id 主键
    * @return 对象
    */
    ProdInfoInfoResponse getInfoById(String id);

    /**
    * 修改产品消息表
    * @param params 产品消息表
    */
    void updateInfo(ProdInfoUpdateRequest params);

    List<ProdListResponse> selectList(String name);

    /**
    * 分页查询
    * @param page 分页对象
    * @param params 参数
    * @return 分页集合
    */
    BasePageResponse<List<ProdInfoPageResponse>> pageInfo(PagingRequest page, ProdInfoPageRequest params);

    ProdInfo infoByIdOrKey(String id, String prodKey);

    /**
     * 获取协议需要的物模型数据
     * @param prodKey 产品key
     * @return 物模型数据
     */
    TmProtocolResponse tmInfo(String prodKey);
}
