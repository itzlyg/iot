package cn.sinozg.applet.controller.protocol;

import cn.sinozg.applet.biz.protocol.service.ProtocolModuleService;
import cn.sinozg.applet.biz.protocol.vo.request.ProtocolModulePageRequest;
import cn.sinozg.applet.biz.protocol.vo.request.ProtocolModuleSaveBaseRequest;
import cn.sinozg.applet.biz.protocol.vo.request.ProtocolModuleScriptRequest;
import cn.sinozg.applet.biz.protocol.vo.request.ProtocolModuleUpdateRequest;
import cn.sinozg.applet.biz.protocol.vo.request.ProtocolStatusRequest;
import cn.sinozg.applet.biz.protocol.vo.request.SelectByNameRequest;
import cn.sinozg.applet.biz.protocol.vo.response.ProtocolModuleInfoResponse;
import cn.sinozg.applet.biz.protocol.vo.response.ProtocolModulePageResponse;
import cn.sinozg.applet.biz.protocol.vo.response.ProtocolModuleScriptResponse;
import cn.sinozg.applet.biz.protocol.vo.response.SaveJarFileResponse;
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
import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/protocol/module")
@Tag(name = "protocol-module-controller", description = "协议组件信息接口")
public class ProtocolModuleController {

    @Resource
    private ProtocolModuleService service;

    /**
    * 新增记录
    * @param request 请求参数
    * @return 是否成功
    */
    @PostMapping("/add")
    @Operation(summary = "新增协议组件信息记录")
    public BaseResponse<String> addProtocolModule(@RequestBody @Valid BaseRequest<ProtocolModuleSaveBaseRequest> request) {
        ProtocolModuleSaveBaseRequest params = MsgUtil.params(request);
        service.createInfo(params);
        return MsgUtil.ok();
    }

    @PostMapping("/uploadJar")
    @Operation(summary = "上传jar包 ", description = "form里文件的name为 uploadFiles ,协议id 为bizId")
    public BaseResponse<SaveJarFileResponse> uploadJar(HttpServletRequest request) {
        SaveJarFileResponse response = service.uploadJar(request);
        return MsgUtil.ok(response);
    }

    /**
    * 删除记录
    * @param request 主键
    * @return 是否成功
    */
    @PostMapping("/delete")
    @Operation(summary = "根据主键删除协议组件信息记录")
    public BaseResponse<String> deleteProtocolModule(@RequestBody @Valid BaseRequest<ComId> request) {
        ComId params = MsgUtil.params(request);
        service.deleteModule(params.getId());
        return MsgUtil.ok();
    }

    /**
    * 查询详情
    * @param request 主键
    * @return 记录信息
    */
    @PostMapping("/detail")
    @Operation(summary = "查询协议组件信息记录")
    public BaseResponse<ProtocolModuleInfoResponse> detailOfProtocolModule(@RequestBody @Valid BaseRequest<ComId> request) {
        ComId params = MsgUtil.params(request);
        ProtocolModuleInfoResponse entity = service.getInfoById(params.getId());
        return MsgUtil.ok(entity);
    }

    /**
    * 更新记录
    * @param request 参数
    * @return 是否成功
    */
    @PostMapping("/update")
    @Operation(summary = "修改协议组件信息记录")
    public BaseResponse<String> updateProtocolModule(@RequestBody @Valid BaseRequest<ProtocolModuleUpdateRequest> request) {
        ProtocolModuleUpdateRequest params = MsgUtil.params(request);
        service.updateInfo(params);
        return MsgUtil.ok();
    }

    @PostMapping("/updateStatus")
    @Operation(summary = "更新协议组件状态")
    public BaseResponse<String> updateStatus(@RequestBody @Valid BaseRequest<ProtocolStatusRequest> request) {
        ProtocolStatusRequest params = MsgUtil.params(request);
        service.updateStatus(params.getId(), params.isEnable());
        return MsgUtil.ok();
    }

    @PostMapping("/getScript")
    @Operation(summary = "获取协议脚本")
    public BaseResponse<ProtocolModuleScriptResponse> getScript(@RequestBody @Valid BaseRequest<ComId> request) {
        ComId params = MsgUtil.params(request);
        ProtocolModuleScriptResponse script = service.getScript(params.getId());
        return MsgUtil.ok(script);
    }

    @PostMapping("/updateScript")
    @Operation(summary = "更新协议脚本")
    public BaseResponse<String> updateScript(@RequestBody @Valid BaseRequest<ProtocolModuleScriptRequest> request) {
        ProtocolModuleScriptRequest params = MsgUtil.params(request);
        service.updateScript(params);
        return MsgUtil.ok();
    }

    @PostMapping("/selectList")
    @Operation(summary = "查询所有的解析脚本")
    public BaseResponse<List<DictListResponse>> selectList(@RequestBody @Valid BaseRequest<SelectByNameRequest> request) {
        SelectByNameRequest params = MsgUtil.params(request);
        List<DictListResponse> list = service.moduleList(params.getName());
        return MsgUtil.ok(list);
    }


    /**
    * 分页查询信息
    * @param request 查询参数
    * @return 分页记录
    */
    @PostMapping("/page_list")
    @Operation(summary = "查询协议组件信息分页列表")
    public BasePageResponse<List<ProtocolModulePageResponse>> pageOfProtocolModule(@RequestBody @Valid BasePageRequest<ProtocolModulePageRequest> request) {
        ProtocolModulePageRequest params = MsgUtil.params(request);
        return service.pageInfo(request.getPage(), params);
    }
}
