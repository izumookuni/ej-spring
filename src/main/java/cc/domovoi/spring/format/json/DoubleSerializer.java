package cc.domovoi.spring.format.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.DecimalFormat;

@Deprecated
public class DoubleSerializer extends JsonSerializer<Double> {

    private static DecimalFormat df = new DecimalFormat("#.00");

    @Override
    public void serialize(Double aDouble, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if (aDouble != null) {
            jsonGenerator.writeString(df.format(aDouble));
        }
    }
}
