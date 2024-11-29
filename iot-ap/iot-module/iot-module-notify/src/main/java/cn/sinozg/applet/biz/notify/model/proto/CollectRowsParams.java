package cn.sinozg.applet.biz.notify.model.proto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 收集器 指标参数
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-23 11:58
 */
@Getter
@Setter
public class CollectRowsParams {
    
    private List<String> columns;

    public int getColumnsCount(){
        if (columns == null) {
            return 0;
        }
        return columns.size();
    }
}
