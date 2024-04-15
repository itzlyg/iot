package cn.sinozg.applet.controller.device;

import cn.sinozg.applet.biz.sink.service.TmMsgSinkService;
import cn.sinozg.applet.biz.sink.vo.request.TmSinkPageRequest;
import cn.sinozg.applet.biz.sink.vo.response.TmSinkPageResponse;
import cn.sinozg.applet.common.core.base.BasePageRequest;
import cn.sinozg.applet.common.core.base.BasePageResponse;
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
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-14 12:26
 */
@Slf4j
@RestController
@RequestMapping("/api/record/sta")
@Tag(name = "iot-record-controller", description = "历史记录、统计相关接口")
public class IotRecordController {

    @Resource
    private TmMsgSinkService tmMsgSinkService;

    @PostMapping("/tm_page_list")
    @Operation(summary = "查询设备事件记录")
    public BasePageResponse<List<TmSinkPageResponse>> pageOfDevice(@RequestBody @Valid BasePageRequest<TmSinkPageRequest> request) {
        TmSinkPageRequest params = MsgUtil.params(request);
        return tmMsgSinkService.typeIdentifierPage(request.getPage(), params);
    }
}
