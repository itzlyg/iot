package cn.sinozg.applet.biz.protocol.vo.valid;

import cn.sinozg.applet.biz.protocol.enums.ProtocolType;
import cn.sinozg.applet.biz.protocol.vo.request.ProtocolModuleSaveBaseRequest;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-22 11:46
 */
public class ProtocolModuleConfigValidationProvider implements DefaultGroupSequenceProvider<ProtocolModuleSaveBaseRequest> {
    @Override
    public List<Class<?>> getValidationGroups(ProtocolModuleSaveBaseRequest params) {
        List<Class<?>> list = new ArrayList<>();
        list.add(ProtocolModuleSaveBaseRequest.class);
        if (params == null) {
            return list;
        }
        list.add(ProtocolType.ofCode(params.getProtocolType()).getValidBean());
        return list;
    }
}
