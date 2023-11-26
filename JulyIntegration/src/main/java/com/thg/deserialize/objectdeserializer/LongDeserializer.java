package com.thg.deserialize.objectdeserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by tanhuigen
 * Date 2022-09-17
 * Description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LongDeserializer extends JsonDeserializer<Long> {

    private final Map<String, Object> dynamicMap;

    public LongDeserializer(Map<String, Object> dynamicMap) {
        this.dynamicMap = dynamicMap;
    }


    @Override
    public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {
        String dynamicString = jsonParser.getText().replaceAll(" ", "");
        if (StringUtils.isBlank(dynamicString)) {
            return null;
        }
        if (dynamicString.startsWith("$")) {
            String realKey = dynamicString.replace("$", "");
            if (dynamicMap.containsKey(realKey)) {
                return Long.parseLong(String.valueOf(dynamicMap.get(realKey)));
            }
        }
        return jsonParser.getValueAsLong();
    }


}
