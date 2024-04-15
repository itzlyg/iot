package cn.sinozg.applet.biz.protocol.service.impl;

import cn.sinozg.applet.biz.protocol.entity.ProtocolJar;
import cn.sinozg.applet.biz.protocol.mapper.ProtocolJarMapper;
import cn.sinozg.applet.biz.protocol.service.ProtocolJarService;
import cn.sinozg.applet.iot.common.utils.ProtocolUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

/**
* 协议对应的jar包 服务实现类
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
@Slf4j
@Service
public class ProtocolJarServiceImpl extends ServiceImpl<ProtocolJarMapper, ProtocolJar> implements ProtocolJarService {

    @Override
    public String idByMd5(String md5) {
        ProtocolJar jar = this.getOne(new LambdaQueryWrapper<ProtocolJar>()
                .eq(ProtocolJar::getMd5, md5));
        if (jar != null) {
            return jar.getId();
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public String saveJar(String id, String md5, String name) {
        ProtocolJar jar = new ProtocolJar();
        jar.setJarName(name);
        jar.setMd5(md5);
        jar.setId(StringUtils.trimToNull(id));
        this.saveOrUpdate(jar);
        return jar.getId();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteJar(String id) {
        ProtocolJar jar = this.getById(id);
        if (jar != null) {
            File file = ProtocolUtil.jarFile(id, jar.getJarName());
            boolean result = ProtocolUtil.deleteFile(file);
            log.info("删除没有引用的jar包结果，{}", result);
            this.removeById(id);
        }
    }
}
