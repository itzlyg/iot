package cn.sinozg.applet.biz.notify.model.collect;

import com.googlecode.aviator.Expression;
import lombok.Data;
import org.apache.commons.lang3.tuple.MutablePair;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-22 14:22
 */
@Data
public class TransformResult {
    private String field;

    private Expression expression;

    private MutablePair<String, String> unit;

    public TransformResult(String field, Expression e){
        this.field = field;
        this.expression = e;
    }

    public TransformResult(String field, String left, String right){
        this.field = field;
        this.unit = MutablePair.of(left, right);
    }
}
