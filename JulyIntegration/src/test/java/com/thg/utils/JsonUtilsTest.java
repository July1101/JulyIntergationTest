package com.thg.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.thg.mock.model.MockData;
import com.thg.deserialize.objectdeserializer.IntegerDeserializer;
import com.thg.deserialize.objectdeserializer.LongDeserializer;
import com.thg.deserialize.objectdeserializer.ObjectDeserializer;
import com.thg.deserialize.objectdeserializer.StringDeserializer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/6/14 23:12
 **/
public class JsonUtilsTest {


    @Test
    public void test1() {

        String host = "127.0.0.1";
        int port = 6379;
        String user = "default";
        String password = "123456";
        JedisPool jedisPool;
        jedisPool = new JedisPool(host,port,user,password);
        Jedis resource = jedisPool.getResource();
        resource.set("1","hello redis!");
        String value = resource.get("1");
        System.out.println("value = " + value);
        jedisPool.close();
    }

    @Test
    public void name() throws JsonProcessingException {
        String json = "[{\"mockId\":\"newDAO\",\"methodParams\":{\"hello\":\"hello1\",\"world\":[],\"map2\":{\"1\":\"tanhuigen\",\"2\":\"lizehao\"}}}]";
        ObjectMapper objectMapper = generaterObjectMapper(null);
        Class<MockData> aClass = MockData.class;
        List<MockData> mockData1 = objectMapper.readValue(json,
            new TypeReference<List<MockData>>() {
            });
        System.out.println("mockData1 = " + mockData1);

    }


    public ObjectMapper generaterObjectMapper(@Nullable Map<String, Object> randomMap) {
        if (randomMap == null) {
            randomMap = new HashMap<>();
        }
        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();
        LongDeserializer longDeserializer = new LongDeserializer(randomMap);
        StringDeserializer stringDeserializer = new StringDeserializer(randomMap);
        IntegerDeserializer integerDeserializer = new IntegerDeserializer(randomMap);
        ObjectDeserializer objectDeserializer = new ObjectDeserializer(randomMap);
        simpleModule.addDeserializer(Long.class, longDeserializer)
            .addDeserializer(long.class, longDeserializer);
        simpleModule.addDeserializer(Integer.class, integerDeserializer)
            .addDeserializer(int.class, integerDeserializer);
        simpleModule.addDeserializer(String.class, stringDeserializer);
//        simpleModule.addDeserializer(Object.class, objectDeserializer);
        objectMapper.registerModules(simpleModule);
        return objectMapper;
    }

}