package com.thg.mock.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.thg.utils.GenericClassUtils;
import com.thg.utils.JsonUtils;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/5/21 20:46
 **/

@Slf4j
@Data
public class TestMock {
    private String mockId;
    private Object bean;
    private Method mockMethod;
    private Method unMockMethod;
    private int mockMethodParamsSize;

    public TestMock(String mockId, Object bean, Method mockMethod, @Nullable Method unMockMethod) {
        this.mockId = mockId;
        this.bean = bean;
        this.mockMethod = mockMethod;
        this.mockMethodParamsSize = mockMethod.getParameterCount();
        this.unMockMethod = unMockMethod;
    }

    public void invokeMockMethod(Map<String,Object> methodParams, ObjectMapper objectMapper) {
        try {
            if (mockMethodParamsSize == 0) {
                mockMethod.invoke(bean);
                return ;
            }
            Object[] paramsObjects = getParamsObjects(methodParams, objectMapper);
            mockMethod.invoke(bean,paramsObjects);
        } catch (Exception e){
            log.error("invokeMockMethod fail");
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }


    public void invokeUnMockMethod(){
        if (unMockMethod != null) {
            try{
                unMockMethod.invoke(bean);
            } catch (ReflectiveOperationException e) {
                log.error("invokeUnMockMethod fail");
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }


    private Object[] getParamsObjects(Map<String, Object> paramsMap, ObjectMapper objectMapper) {
        List<Object> realParams = new ArrayList<>();
        String[] parameterNames = GenericClassUtils.getParameterNames(mockMethod);
        Type[] parameterTypes = mockMethod.getGenericParameterTypes();
        if (paramsMap.size() != parameterNames.length) {
            log.error("there mockData params size do not matches method params size");
            Assert.fail("there mockData params size do not matches method params size");
        }
        for (int i = 0; i < parameterNames.length; i++) {
            if (!paramsMap.containsKey(parameterNames[i])) {
                log.error("There is no params named:{}", parameterNames[i]);
                Assert.fail();
            }
            Object originValue = paramsMap.get(parameterNames[i]);
            JavaType javaType = TypeFactory.defaultInstance().constructType(parameterTypes[i]);
            JsonUtils.deserialize(originValue, javaType, objectMapper);
            realParams.add(JsonUtils.deserialize(originValue, javaType, objectMapper));
        }
        return realParams.toArray();
    }

}
