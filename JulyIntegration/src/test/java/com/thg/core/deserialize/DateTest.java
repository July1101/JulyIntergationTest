package com.thg.core.deserialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/3/26 17:33
 **/
public class DateTest {



    public void doAction(String a1,List<String> a4, Map<Integer,String> a5){

    }


    @Test
    public void newTest() {

        String str = "[CID:123]asda456";
        String regEx = "\\[CID:\\d+\\](.*)";
        Pattern p = Pattern.compile(regEx);
        Matcher matcher = p.matcher(str);

        if(matcher.find()){
            System.out.println("matcher.group() = " + matcher.group());
        }


        String result = matcher.replaceAll("").trim();
        System.out.println("result = " + result);

        System.out.println(str.matches(regEx));

    }

    @Test
    public void name() throws JsonProcessingException {
        Method[] declaredMethods = DateTest.class.getDeclaredMethods();
        Method method = declaredMethods[0];
            List<Class<?>> classList = Arrays.stream(method.getParameterTypes())
                .collect(Collectors.toList());
            Type[] genericParameterTypes = method.getGenericParameterTypes();

            Long a1 = 1L;
            List<String> a4 = Arrays.asList("123","234","456");
            Map<Integer,String> a5 = new HashMap<>();
            a5.put(1,"456");
            ObjectMapper mapper = new ObjectMapper();
            JavaType javaType = mapper.getTypeFactory().constructType(genericParameterTypes[1]);
            Object b4 = mapper.readValue(mapper.writeValueAsString(a4), javaType);
            Assert.assertEquals(a4,b4);

            JavaType javaType2 = mapper.getTypeFactory().constructType(genericParameterTypes[2]);
        Object b5 = mapper.readValue(mapper.writeValueAsString(a5), javaType2);
        Assert.assertEquals(a5,b5);

        for (Type genericParameterType : genericParameterTypes) {
                JavaType type = mapper.getTypeFactory().constructType(genericParameterType);
            }
//            ParameterizedType type = (ParameterizedType) field.getGenericType();
//            Stream.of(type.getActualTypeArguments()).forEach(System.out::println);
//
//            field = clazz.getField("list1");
//            type = (ParameterizedType) field.getGenericType();
//            Stream.of(type.getActualTypeArguments()).forEach(System.out::println);

//            String[] parameterNames = MockUtils.getParameterNames(method);
//            for (int i = 0; i < parameterNames.length; i++) {
//                System.out.println("parameterNames[i] = " + parameterNames[i]);
//            }
    }
}
