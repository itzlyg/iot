package cn.sinozg.applet.controller.protocol;

import cn.sinozg.applet.biz.protocol.service.AnalysisScriptService;
import cn.sinozg.applet.biz.protocol.vo.request.AnalysisScriptCreateRequest;
import cn.sinozg.applet.biz.protocol.vo.request.AnalysisScriptPageRequest;
import cn.sinozg.applet.biz.protocol.vo.request.AnalysisScriptUpdateRequest;
import cn.sinozg.applet.biz.protocol.vo.request.AnalysisScriptUpdateTextRequest;
import cn.sinozg.applet.biz.protocol.vo.request.ProtocolStatusRequest;
import cn.sinozg.applet.biz.protocol.vo.request.SelectByNameRequest;
import cn.sinozg.applet.biz.protocol.vo.response.AnalysisScriptPageResponse;
import cn.sinozg.applet.biz.system.vo.response.DictListResponse;
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
 * @author xieyubin
 * @since 2023-11-20 18:41:52
 */
@RestController
@RequestMapping("/api/protocol/analysis")
@Tag(name = "analysis-script-controller", description = "数据解析脚本接口")
public class AnalysisScriptController {

    @Resource
    private AnalysisScriptService service;

    /**
    * 新增记录
    * @param request 请求参数
    * @return 是否成功
    */
    @PostMapping("/add")
    @Operation(summary = "新增数据解析脚本记录")
    public BaseResponse<String> addAnalysisScript(@RequestBody @Valid BaseRequest<AnalysisScriptCreateRequest> request) {
        AnalysisScriptCreateRequest params = MsgUtil.params(request);
        service.createInfo(params);
        return MsgUtil.ok();
    }

    /**
    * 删除记录
    * @param request 主键
    * @return 是否成功
    */
    @PostMapping("/delete")
    @Operation(summary = "根据主键删除数据解析脚本记录")
    public BaseResponse<String> deleteAnalysisScript(@RequestBody @Valid BaseRequest<ComId> request) {
        ComId params = MsgUtil.params(request);
        service.deleteScript(params.getId());
        return MsgUtil.ok();
    }

    /**
    * 查询详情
    * @param request 主键
    * @return 记录信息
    */
    @PostMapping("/getScript")
    @Operation(summary = "获取脚本信息")
    public BaseResponse<String> analysisScript(@RequestBody @Valid BaseRequest<ComId> request) {
        ComId params = MsgUtil.params(request);
        String script = service.getScriptById(params.getId());
        return MsgUtil.ok(script);
    }

    @PostMapping("/updateScript")
    @Operation(summary = "更新脚本信息")
    public BaseResponse<String> updateScript(@RequestBody @Valid BaseRequest<AnalysisScriptUpdateTextRequest> request) {
        AnalysisScriptUpdateTextRequest params = MsgUtil.params(request);
        service.updateScript(params);
        return MsgUtil.ok();
    }


    /**
    * 更新记录
    * @param request 参数
    * @return 是否成功
    */
    @PostMapping("/update")
    @Operation(summary = "修改数据解析脚本记录")
    public BaseResponse<String> updateAnalysisScript(@RequestBody @Valid BaseRequest<AnalysisScriptUpdateRequest> request) {
        AnalysisScriptUpdateRequest params = MsgUtil.params(request);
        service.updateInfo(params);
        return MsgUtil.ok();
    }

    @PostMapping("/updateStatus")
    @Operation(summary = "更新数据解析脚本状态")
    public BaseResponse<String> updateStatus(@RequestBody @Valid BaseRequest<ProtocolStatusRequest> request) {
        ProtocolStatusRequest params = MsgUtil.params(request);
        service.updateStatus(params.getId(), params.isEnable());
        return MsgUtil.ok();
    }


    @PostMapping("/selectList")
    @Operation(summary = "查询所有的解析脚本")
    public BaseResponse<List<DictListResponse>> selectList(@RequestBody @Valid BaseRequest<SelectByNameRequest> request) {
        SelectByNameRequest params = MsgUtil.params(request);
        List<DictListResponse> list = service.scriptList(params);
        return MsgUtil.ok(list);
    }


    /**
    * 分页查询信息
    * @param request 查询参数
    * @return 分页记录
    */
    @PostMapping("/page_list")
    @Operation(summary = "查询数据解析脚本分页列表")
    public BasePageResponse<List<AnalysisScriptPageResponse>> pageOfAnalysisScript(@RequestBody @Valid BasePageRequest<AnalysisScriptPageRequest> request) {
        AnalysisScriptPageRequest params = MsgUtil.params(request);
        return service.pageInfo(request.getPage(), params);
    }
}
