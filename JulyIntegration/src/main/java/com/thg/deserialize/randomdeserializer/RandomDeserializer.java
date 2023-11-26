package com.thg.deserialize.randomdeserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.thg.utils.DateDeserializerUtils;
import com.thg.utils.RandomUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by tanhuigen
 * Date 2022-09-17
 * Description
 */
public class RandomDeserializer extends JsonDeserializer<Object> {

    private static final int MAX_STRING_LENGTH = 5000;
    private static final int DEFAULT_STRING_LENGTH = 10;

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String dynamicString = jsonParser.getText().replace(" ","");
        if(StringUtils.isBlank(dynamicString)){
            return null;
        }
        List<String> patternResults = new ArrayList<>();
        if(dynamicString.startsWith("int") || dynamicString.startsWith("INT")){
            int paramsNumber = patternSearch(dynamicString,patternResults);
            if(paramsNumber>1){
                return RandomUtils.nextInt(Integer.parseInt(patternResults.get(0)),Integer.parseInt(patternResults.get(1)));
            }
            return RandomUtils.nextInt();
        }
        else if(dynamicString.startsWith("long") || dynamicString.startsWith("Long")){
            int paramsNumber = patternSearch(dynamicString,patternResults);
            if(paramsNumber>1){
                return RandomUtils.nextLong(Long.parseLong(patternResults.get(0)),Long.parseLong(patternResults.get(1)));
            }
            return RandomUtils.nextLong();
        } else if (dynamicString.startsWith("str") || dynamicString.startsWith("string") ||
            dynamicString.startsWith("STR") || dynamicString.startsWith("STRING")) {
            int paramsNumber = patternSearch(dynamicString, patternResults);
            List<Integer> integerList = new ArrayList<>();
            for (String patternResult : patternResults) {
                int result = Integer.parseInt(patternResult);
                if (result <= 0 || result > MAX_STRING_LENGTH) {
                    return null;
                }
                integerList.add(result);
            }
            switch (paramsNumber) {
                case 0:
                    return RandomUtils.nextString(DEFAULT_STRING_LENGTH);
                case 1:
                    return RandomUtils.nextString(integerList.get(0));
                default:
                    return RandomUtils.nextString(integerList.get(0), integerList.get(1));
            }
        } else if (dynamicString.startsWith("#")) {
            return DateDeserializerUtils.parseDate(dynamicString);
        }
        return dynamicString;
    }

    private int patternSearch(String toSearch, List<String> results){
        String patternString = "(\\d+)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(toSearch);
        int res = 0;
        while(matcher.find()){
            results.add(matcher.group());
            res++;
        }
        return res;
    }
}
