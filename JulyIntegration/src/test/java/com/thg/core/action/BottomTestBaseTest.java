package com.thg.core.action;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.thg.deserialize.objectdeserializer.IntegerDeserializer;
import com.thg.deserialize.objectdeserializer.LongDeserializer;
import com.thg.deserialize.objectdeserializer.ObjectDeserializer;
import com.thg.deserialize.objectdeserializer.StringDeserializer;
import lombok.Data;
import org.junit.Test;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/6/20 22:27
 **/
public class BottomTestBaseTest {


    @Test
    public void name() throws JsonProcessingException {

        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();
        MyS myS = new MyS();
        simpleModule.addDeserializer(String.class, myS);
        objectMapper.registerModules(simpleModule, new JavaTimeModule());

        String json = "{\"info\":{\"name\":\"tanhuigen\",\"age\":18}}";
        String json2 = "{\"info\":\"hello\"}";
        TestInfo testInfo = objectMapper.readValue(json2, TestInfo.class);
        System.out.println("testInfo = " + testInfo);
        System.out.println("testInfo.getInfo().getClass() = " + testInfo.getInfo().getClass());
        TestInfo info = new ObjectMapper().readValue(json, TestInfo.class);
        System.out.println("info.getInfo().getClass() = " + info.getInfo().getClass());
        System.out.println("info = " + info);
    }

    @Data
    public static class TestInfo{
        Object info;
    }


}