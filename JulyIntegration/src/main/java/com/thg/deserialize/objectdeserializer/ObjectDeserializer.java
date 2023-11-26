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
 * Date 2022-10-29
 * Description 
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ObjectDeserializer extends JsonDeserializer<Object> {

    private final Map<String, Object> dynamicMap;

    public ObjectDeserializer(Map<String, Object> dynamicMap) {
        this.dynamicMap = dynamicMap;
    }

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {
        String dynamicString = jsonParser.getText().replaceAll(" ","");
        if (StringUtils.isBlank(dynamicString)) {
            return null;
        }
        if(dynamicString.startsWith("$")){
            String key = dynamicString.replace("$","");
            if(dynamicMap.containsKey(key)){
                return dynamicMap.get(key);
            }
        }
        return jsonParser.getValueAsString();
    }
}
