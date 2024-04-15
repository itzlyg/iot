package cn.sinozg.applet.biz.system.mapper;

import cn.sinozg.applet.biz.system.entity.CorpInfo;
import cn.sinozg.applet.common.core.model.DictDataVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* 租户消息表 Mapper 接口
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-09 16:04:37
*/
public interface CorpInfoMapper extends BaseMapper<CorpInfo> {

    /**
     * 查询所以的租户
     * @return 租户下拉
     */
    List<DictDataVo> corpList ();
}
