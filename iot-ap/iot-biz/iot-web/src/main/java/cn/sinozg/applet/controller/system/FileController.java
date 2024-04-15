package cn.sinozg.applet.controller.system;

import cn.sinozg.applet.biz.system.service.FileMappingService;
import cn.sinozg.applet.biz.system.vo.request.DeleteFileRequest;
import cn.sinozg.applet.biz.system.vo.request.LargeFileRequest;
import cn.sinozg.applet.biz.system.vo.response.FileInfoResponse;
import cn.sinozg.applet.biz.system.vo.response.LargeFileResponse;
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

/**
 * 文件控制器
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-08-29 14:59
 */
@RestController
@RequestMapping("/api/sys/file")
@Tag(name = "file-controller", description = "文件映射表接口")
public class FileController {

    @Resource
    private FileMappingService service;

    @Operation(summary = "上传文件", description = "form里文件的name为 uploadFiles ,业务id 为bizId")
    @PostMapping(value = "/upload")
    public BaseResponse<FileInfoResponse> upload(HttpServletRequest request) {
        FileInfoResponse result = service.addFile(request);
        return MsgUtil.ok(result);
    }

    @Operation(summary = "大文件上传前获取上传信息")
    @PostMapping(value = "/upload_before")
    public BaseResponse<LargeFileResponse> fileInfo(@RequestBody @Valid BaseRequest<LargeFileRequest> request) {
        LargeFileRequest params = MsgUtil.params(request);
        LargeFileResponse t = service.largeFileUpload(params);
        return MsgUtil.ok(t);
    }


    @Operation(summary = "大文件上传后保存信息 参数为返回的id")
    @PostMapping(value = "/upload_save")
    public BaseResponse<FileInfoResponse> uploadSave(@RequestBody @Valid BaseRequest<ComId> request) {
        ComId params = MsgUtil.params(request);
        FileInfoResponse response = service.saveLargeFile(params.getId());
        return MsgUtil.ok(response);
    }


    @Operation(summary = "删除文件")
    @PostMapping(value = "/deleteFile")
    public BaseResponse<Boolean> deleteFile(@RequestBody @Valid BaseRequest<DeleteFileRequest> request) {
        DeleteFileRequest params = MsgUtil.params(request);
        boolean t = service.deleteFile(params);
        return MsgUtil.ok(t);
    }
}
