package cn.sinozg.applet.biz.system.service;

import cn.sinozg.applet.biz.system.entity.CorpInfo;
import cn.sinozg.applet.biz.system.vo.request.CropCreateRequest;
import cn.sinozg.applet.common.core.model.DictDataVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* 租户消息表 服务类
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-09 16:04:37
*/
public interface CorpInfoService extends IService<CorpInfo> {

    /**
     * 创建租户
     * @param params 租户消息
     */
    void createCrop(CropCreateRequest params);

    /**
     * 查询所以的租户
     * @return 租户下拉
     */
    List<DictDataVo> corpList ();
}
