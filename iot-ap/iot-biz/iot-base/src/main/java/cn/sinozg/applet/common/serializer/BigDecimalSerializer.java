package cn.sinozg.applet.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-10-25 16:54
 */
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeObject(BigDecimal.ZERO);
        } else {
            String s = value.stripTrailingZeros().toPlainString();
            BigDecimal b = new BigDecimal(s);
            BigDecimal bd = b.setScale(2, RoundingMode.HALF_UP);
            gen.writeObject(bd);
        }
    }
}
