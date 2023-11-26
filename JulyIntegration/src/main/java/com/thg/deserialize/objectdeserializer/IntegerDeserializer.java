package com.thg.deserialize.objectdeserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by tanhuigen
 * Date 2022-09-24
 * Description
 */
public class IntegerDeserializer extends JsonDeserializer<Integer> {

    private final Map<String, Object> dynamicMap;

    public IntegerDeserializer(Map<String, Object> dynamicMap) {
        this.dynamicMap = dynamicMap;
    }

    @Override
    public Integer deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {
        String dynamicString = jsonParser.getText().replaceAll(" ","");
        if (StringUtils.isBlank(dynamicString)) {
            return null;
        }
        if (dynamicString.startsWith("$")) {
            String realKey = dynamicString.replace("$", "");
            if (dynamicMap.containsKey(realKey)) {
                return Integer.parseInt(String.valueOf(dynamicMap.get(realKey)));
            }
        }
        return jsonParser.getValueAsInt();
    }
}
