package cn.sinozg.applet.controller.system;

import cn.sinozg.applet.biz.opt.service.OptLogDataService;
import cn.sinozg.applet.biz.opt.vo.request.OptLogClearRequest;
import cn.sinozg.applet.biz.opt.vo.request.OptLogDataByBizIdPageRequest;
import cn.sinozg.applet.biz.opt.vo.request.OptLogDataPageRequest;
import cn.sinozg.applet.biz.opt.vo.response.OptLogDataByBizIdPageResponse;
import cn.sinozg.applet.biz.opt.vo.response.OptLogDataPageResponse;
import cn.sinozg.applet.common.core.base.BasePageRequest;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.BaseRequest;
import cn.sinozg.applet.common.core.base.BaseResponse;
import cn.sinozg.applet.common.core.model.ComId;
import cn.sinozg.applet.common.utils.MsgUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 接口日志 数据日志接口
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-03-05 19:36
 */
@Slf4j
@RestController
@RequestMapping("/api/opt/log")
@Tag(name = "opt-log-controller", description = "操作日志、数据日志接口")
public class OptLogController {

    @Resource
    private OptLogDataService optLogDataService;

    @PostMapping("/detail")
    @Operation(summary = "根据日志id查询")
    public BaseResponse<OptLogDataByBizIdPageResponse> detail(@RequestBody @Valid BaseRequest<ComId> request) {
        ComId params = MsgUtil.params(request);
        OptLogDataByBizIdPageResponse response = optLogDataService.logDataDetailById(params.getId());
        return MsgUtil.ok(response);
    }

    @PostMapping("/clear")
    @Operation(summary = "清除日志")
    public BaseResponse<String> clear(@RequestBody @Valid BaseRequest<OptLogClearRequest> request) {
        OptLogClearRequest params = MsgUtil.params(request);
        optLogDataService.clear(params);
        return MsgUtil.ok();
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询")
    public BasePageResponse<List<OptLogDataPageResponse>> dictList(@RequestBody @Valid BasePageRequest<OptLogDataPageRequest> request) {
        OptLogDataPageRequest params = MsgUtil.params(request);
        return optLogDataService.pageInfo(request.getPage(), params);
    }

    @PostMapping("/pageByBizId")
    @Operation(summary = "根据业务id查询")
    public BasePageResponse<List<OptLogDataByBizIdPageResponse>> pageByBizId(@RequestBody @Valid BasePageRequest<OptLogDataByBizIdPageRequest> request) {
        OptLogDataByBizIdPageRequest params = MsgUtil.params(request);
        return optLogDataService.logDataDetailByBizId(request.getPage(), params);
    }
}
