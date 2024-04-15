package cn.sinozg.applet.controller.product;

import cn.sinozg.applet.biz.product.service.ProdInfoService;
import cn.sinozg.applet.biz.product.vo.request.ProdInfoCreateRequest;
import cn.sinozg.applet.biz.product.vo.request.ProdInfoPageRequest;
import cn.sinozg.applet.biz.product.vo.request.ProdInfoUpdateRequest;
import cn.sinozg.applet.biz.product.vo.response.ProdInfoInfoResponse;
import cn.sinozg.applet.biz.product.vo.response.ProdInfoPageResponse;
import cn.sinozg.applet.biz.product.vo.response.ProdListResponse;
import cn.sinozg.applet.common.core.base.BasePageRequest;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.BaseRequest;
import cn.sinozg.applet.common.core.base.BaseResponse;
import cn.sinozg.applet.common.core.model.ComId;
import cn.sinozg.applet.common.utils.MsgUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 前端控制器
 * @Description
 * @Copyright Copyright (c) 2023
 * @author zy
 * @since 2023-11-27 21:44:29
 */
@RestController
@RequestMapping("/api/product/prod_info")
@Tag(name = "prod-info-controller", description = "产品消息表接口")
public class ProdInfoController {

    @Resource
    private ProdInfoService service;

    /**
    * 新增记录
    * @param request 请求参数
    * @return 是否成功
    */
    @PostMapping("/add")
    @Operation(summary = "新增产品消息表记录")
    public BaseResponse<String> addProd(@RequestBody @Valid BaseRequest<ProdInfoCreateRequest> request) {
        ProdInfoCreateRequest params = MsgUtil.params(request);
        service.createInfo(params);
        return MsgUtil.ok();
    }

    /**
    * 删除记录
    * @param request 主键
    * @return 是否成功
    */
    @PostMapping("/delete")
    @Operation(summary = "根据主键删除产品消息表记录")
    public BaseResponse<String> deleteProd(@RequestBody @Valid BaseRequest<ComId> request) {
        ComId params = MsgUtil.params(request);
        service.removeById(params.getId());
        return MsgUtil.ok();
    }

    /**
    * 查询详情
    * @param request 主键
    * @return 记录信息
    */
    @PostMapping("/detail")
    @Operation(summary = "查询产品消息表记录")
    public BaseResponse<ProdInfoInfoResponse> detailOfProd(@RequestBody @Valid BaseRequest<ComId> request) {
        ComId params = MsgUtil.params(request);
        ProdInfoInfoResponse entity = service.getInfoById(params.getId());
        return MsgUtil.ok(entity);
    }

    /**
    * 更新记录
    * @param request 参数
    * @return 是否成功
    */
    @PostMapping("/update")
    @Operation(summary = "修改产品消息表记录")
    public BaseResponse<String> updateProd(@RequestBody @Valid BaseRequest<ProdInfoUpdateRequest> request) {
        ProdInfoUpdateRequest params = MsgUtil.params(request);
        service.updateInfo(params);
        return MsgUtil.ok();
    }

    @PostMapping("/selectList")
    @Operation(summary = "列表查询")
    public BaseResponse<List<ProdListResponse>> selectList(@RequestBody @Valid BaseRequest<ComId> request) {
        ComId params = MsgUtil.params(request);
        List<ProdListResponse> list = service.selectList(params.getId());
        return MsgUtil.ok(list);
    }


    /**
    * 分页查询信息
    * @param request 查询参数
    * @return 分页记录
    */
    @PostMapping("/page_list")
    @Operation(summary = "查询产品消息表分页列表")
    public BasePageResponse<List<ProdInfoPageResponse>> pageOfProd(@RequestBody @Valid BasePageRequest<ProdInfoPageRequest> request) {
        ProdInfoPageRequest params = MsgUtil.params(request);
        return service.pageInfo(request.getPage(), params);
    }
}
