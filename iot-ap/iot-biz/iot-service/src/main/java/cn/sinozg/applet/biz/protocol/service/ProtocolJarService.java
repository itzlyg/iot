package cn.sinozg.applet.biz.protocol.service;

import cn.sinozg.applet.biz.protocol.entity.ProtocolJar;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* 协议对应的jar包 服务类
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
public interface ProtocolJarService extends IService<ProtocolJar> {

    String idByMd5(String md5);

    String saveJar (String id, String md5, String name);

    void deleteJar(String id);
}
