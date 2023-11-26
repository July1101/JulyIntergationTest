package com.thg.redis.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.thg.config.properties.JulyRedisProperties.ConvertProperty;
import com.thg.utils.JsonUtils;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/7/2 20:13
 **/
public class DefaultRedisSerializer implements JulyRedisSerializer<Object> {


    public final String className;
    private final JavaType javaType;

    public DefaultRedisSerializer(ConvertProperty property) {
        this.className = property.getName();
        Class<?> classForName = getClassForName(property.getPath());
        this.javaType = TypeFactory.defaultInstance().constructType(classForName);
    }

    @Override
    public String className() {
        return className;
    }

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        try {
            return new ObjectMapper().writeValueAsBytes(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        return JsonUtils.deserializeBytes(bytes,javaType);
    }

    private Class<?> getClassForName(String path) {
        Class<?> clazz;
        try {
            clazz = Class.forName(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return clazz;
    }
}
