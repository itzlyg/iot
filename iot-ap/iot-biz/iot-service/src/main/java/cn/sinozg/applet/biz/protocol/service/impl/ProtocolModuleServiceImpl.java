package cn.sinozg.applet.biz.protocol.service.impl;

import cn.sinozg.applet.biz.protocol.entity.ProtocolJar;
import cn.sinozg.applet.biz.protocol.entity.ProtocolModule;
import cn.sinozg.applet.biz.protocol.enums.ProtocolType;
import cn.sinozg.applet.biz.protocol.mapper.ProtocolModuleMapper;
import cn.sinozg.applet.biz.protocol.service.ProtocolConfigService;
import cn.sinozg.applet.biz.protocol.service.ProtocolJarService;
import cn.sinozg.applet.biz.protocol.service.ProtocolModuleService;
import cn.sinozg.applet.biz.protocol.vo.module.BaseProtocolConfig;
import cn.sinozg.applet.biz.protocol.vo.request.ProtocolModulePageRequest;
import cn.sinozg.applet.biz.protocol.vo.request.ProtocolModuleSaveBaseRequest;
import cn.sinozg.applet.biz.protocol.vo.request.ProtocolModuleScriptRequest;
import cn.sinozg.applet.biz.protocol.vo.request.ProtocolModuleSelectRequest;
import cn.sinozg.applet.biz.protocol.vo.request.ProtocolModuleUpdateRequest;
import cn.sinozg.applet.biz.protocol.vo.response.ProtocolModuleInfoResponse;
import cn.sinozg.applet.biz.protocol.vo.response.ProtocolModulePageResponse;
import cn.sinozg.applet.biz.protocol.vo.response.ProtocolModuleScriptResponse;
import cn.sinozg.applet.biz.protocol.vo.response.SaveJarFileResponse;
import cn.sinozg.applet.biz.system.service.FileMappingService;
import cn.sinozg.applet.biz.system.vo.response.DictListResponse;
import cn.sinozg.applet.biz.system.vo.response.FileUploadTempResponse;
import cn.sinozg.applet.common.constant.BaseConstants;
import cn.sinozg.applet.common.constant.Constants;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.PagingRequest;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.BizUtil;
import cn.sinozg.applet.common.utils.FileUtil;
import cn.sinozg.applet.common.utils.PageUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.iot.common.utils.ProtocolUtil;
import cn.sinozg.applet.iot.protocol.achieve.DeviceProtocolComponent;
import cn.sinozg.applet.iot.protocol.service.DeviceProtocolDataService;
import cn.sinozg.applet.iot.protocol.vo.response.ProtocolModuleRegisterResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;

/**
* 协议组件信息 服务实现类
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
@Slf4j
@Service
public class ProtocolModuleServiceImpl extends ServiceImpl<ProtocolModuleMapper, ProtocolModule> implements ProtocolModuleService {
    private static final int MAX_M = 5;
    private static final int MAX_SIZE = MAX_M * 1024 * 1024;

    @Resource
    private ProtocolModuleMapper mapper;

    @Resource
    private ProtocolJarService jarService;

    @Resource
    private FileMappingService mappingService;

    @Resource
    private ProtocolConfigService configService;

    @Resource
    private DeviceProtocolComponent protocolComponent;

    @Resource
    private DeviceProtocolDataService protocolDataService;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void createInfo(ProtocolModuleSaveBaseRequest params) {
        ProtocolModule entity = PojoUtil.copyBean(params, ProtocolModule.class);
        entity.setDataStatus(Constants.STATUS_00);
        this.save(entity);
        configService.saveProtocolConfig(entity.getId(), params);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public SaveJarFileResponse uploadJar(HttpServletRequest request){
        FileUploadTempResponse fileTemp = mappingService.fileInfo(request);
        String moduleId = fileTemp.getBizId();
        MultipartFile file = fileTemp.getFile();
        SaveJarFileResponse response = new SaveJarFileResponse();
        if (file.getSize() > MAX_SIZE) {
            throw new CavException("文件超过{}M！", MAX_M);
        }
        log.info("文件大小为：{}", file.getSize());
        File f = FileUtil.multipartFileToFile(file);
        String fileSuffix = ".jar";
        if (!FileUtil.judgeFile(f,fileSuffix)) {
            throw new CavException("上传文件不为jar包，请检查！");
        }
        ProtocolModule module;
        ProtocolJar jarDb = null;
        if (StringUtils.isNotBlank(moduleId)) {
            // 查询本地的已经有的jar包
            module = this.infoById(moduleId);
            jarDb = jarService.getById(module.getJarId());
            if (jarDb == null) {
                throw new CavException("未找到组件的jar包！");
            }
        }
        String fileName = file.getOriginalFilename();
        response.setJarName(fileName);
        String jarId;
        File tempFile = FileUtil.multipartFileToFile(file);
        try (InputStream md5Is = Files.newInputStream(tempFile.toPath())) {
            String md5 = DigestUtils.md5Hex(md5Is);
            // 修改模式
            if (StringUtils.isNotBlank(moduleId)) {
                assert jarDb != null;
                if (jarDb.getMd5().equals(md5)) {
                    if (jarDb.getJarName().equals(fileName)) {
                        throw new CavException("jar包没有改变！");
                    } else {
                        jarId = jarService.saveJar(jarDb.getId(), md5, fileName);
                    }
                } else {
                    jarId = saveJarByMd5(md5, fileName, tempFile);
                }
                // 新增
            } else {
                jarId = saveJarByMd5(md5, fileName, tempFile);
            }
            response.setJarId(jarId);
        } catch (Exception e) {
            if (e instanceof CavException) {
                throw new CavException(e);
            } else {
                log.error("保存jar包失败！", e);
                throw new CavException("保存jar包失败！");
            }
        }
        return response;
    }

    @Override
    public ProtocolModuleInfoResponse getInfoById(String id) {
        ProtocolModule entity = this.infoById(id);
        ProtocolModuleInfoResponse module = PojoUtil.copyBean(entity, ProtocolModuleInfoResponse.class);
        BaseProtocolConfig config = configService.getInfoByProtocolId(id, module.getProtocolType());
        ProtocolType type = ProtocolType.ofCode(module.getProtocolType());
        PojoUtil.setProperty(module, type.getFieldName(), config);
        return module;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void updateInfo(ProtocolModuleUpdateRequest params) {
        ProtocolModule dbInfo = this.infoById(params.getId());
        if (Constants.STATUS_01.equals(dbInfo.getDataStatus())) {
            throw new CavException("组件启用中，无法修改！");
        }
        ProtocolModule entity = PojoUtil.copyBean(params, ProtocolModule.class);
        configService.saveProtocolConfig(params.getId(), params);
        this.updateById(entity);
        if (!StringUtils.equals(dbInfo.getJarId(), entity.getJarId())) {
            // 试图删除旧jar包信息
            this.deleteJar(dbInfo.getJarId(), params.getId());
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteModule(String id){
        ProtocolModule entity = this.infoById(id);
        if (Constants.STATUS_01.equals(entity.getDataStatus())) {
            throw new CavException("组件启用中，无法删除！");
        }
        this.removeById(id);
        File scriptFile = ProtocolUtil.scriptFile(id, BaseConstants.STATUS_00);
        ProtocolUtil.deleteFile(scriptFile);
        this.deleteJar(entity.getJarId(), id);
    }

    @Override
    public void updateStatus (String id, boolean enable){
        ProtocolModule entity = this.infoById(id);
        if (enable && Constants.STATUS_01.equals(entity.getDataStatus())) {
            throw new CavException("组件已经启用！");
        }
        if (!enable && Constants.STATUS_00.equals(entity.getDataStatus())) {
            throw new CavException("组件已经停止！");
        }
        ProtocolModule update = new ProtocolModule();
        update.setId(id);
        if (enable) {
            File scriptFile = ProtocolUtil.scriptFile(id, Constants.STATUS_00);
            if (!scriptFile.exists()) {
                throw new CavException("请先编写组件脚本！");
            }
            ProtocolModuleRegisterResponse module = PojoUtil.copyBean(entity, ProtocolModuleRegisterResponse.class);
            // 重新注册
            protocolDataService.registerModuleInfo(module);
            protocolComponent.register(module);
            protocolComponent.start(id);
            update.setDataStatus(Constants.STATUS_01);
        } else {
            protocolComponent.deRegister(id);
            update.setDataStatus(Constants.STATUS_00);
        }
        this.updateById(update);
    }

    @Override
    public ProtocolModuleScriptResponse getScript(String id){
        ProtocolModule entity = this.infoById(id);
        ProtocolModuleScriptResponse response = new ProtocolModuleScriptResponse();
        response.setId(id);
        response.setProtocolScriptType(entity.getProtocolScriptType());
        String script = ProtocolUtil.readScript(id, false);
        response.setScript(script);
        return response;
    }

    @Override
    public void updateScript(ProtocolModuleScriptRequest params) {
        String id = params.getId();
        ProtocolModule entity = this.infoById(id);
        if (Constants.STATUS_01.equals(entity.getDataStatus())) {
            throw new CavException("组件启用中，无法修改脚本！");
        }
        ProtocolUtil.writeScript(id, false, params.getScript());
        ProtocolModule update = new ProtocolModule();
        update.setId(id);
        update.setProtocolScriptType(params.getProtocolScriptType());
        this.updateById(update);
    }

    @Override
    public List<DictListResponse> moduleList(String name) {
        ProtocolModuleSelectRequest param = new ProtocolModuleSelectRequest();
        param.setProtocolName(name);
        param.setUid(BizUtil.demoUser());
        return mapper.moduleList(param);
    }

    @Override
    public BasePageResponse<List<ProtocolModulePageResponse>> pageInfo(PagingRequest page, ProtocolModulePageRequest params) {
        PageUtil<ProtocolModulePageResponse, ProtocolModulePageRequest> pu = (p, q) -> mapper.pageInfo(p, q);
        params.setUid(BizUtil.demoUser());
        return pu.page(page, params);
    }


    /**
     * 删除无用的jar信息
     * @param jarId jarid
     * @param excludeId 要排除的引用
     */
    private void deleteJar(String jarId, String excludeId){
        if (StringUtils.isBlank(jarId)) {
            return;
        }
        List<ProtocolModule> list = this.list(new LambdaQueryWrapper<ProtocolModule>()
                .eq(ProtocolModule::getJarId, jarId)
                .notIn(StringUtils.isNotBlank(excludeId), ProtocolModule::getId, excludeId));
        // 可以删除对应的记录和文件
        if (CollectionUtils.isEmpty(list)) {
            jarService.deleteJar(jarId);
        }
    }

    /**
     * 如果没有对应的jar 或者传过来的数据的 md5与原始的md5不一样
     * @param md5 md5
     * @param fileName 文件名称
     * @param file 文件
     * @return 文件id
     */
    private String saveJarByMd5(String md5, String fileName, File file) {
        String jarIdDb = jarService.idByMd5(md5);
        String jarId = jarIdDb;
        // 没有一样的jar包
        if (StringUtils.isBlank(jarIdDb)) {
            jarId = jarService.saveJar(jarId, md5, fileName);
            File localFile = ProtocolUtil.jarFile(jarId, fileName);
            try (InputStream fis = Files.newInputStream(file.toPath());
                    OutputStream out = FileUtils.newOutputStream(localFile, false)){
                FileUtil.writeBuffer(fis, out);
            } catch (Exception e) {
                log.error("复制jar到本地失败！", e);
                throw new CavException("复制jar到本地失败！");
            }
        }
        return jarId;
    }

    /**
     * 查询详情
     * @param id 主键id
     * @return 实体对象
     */
    private ProtocolModule infoById(String id){
        ProtocolModule entity = this.getById(id);
        if (entity == null) {
            throw new CavException("未找到协议组件信息！");
        }
        return entity;
    }
}
