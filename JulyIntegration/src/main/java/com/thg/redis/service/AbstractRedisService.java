package com.thg.redis.service;

import com.fasterxml.jackson.databind.JavaType;
import com.thg.redis.config.RedisConfigFactory;
import com.thg.redis.model.RedisBase;
import com.thg.utils.JsonUtils;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/6/24 21:31
 **/
public abstract class AbstractRedisService implements RedisService {

    private final RedisConfigFactory redisConfigFactory;

    protected AbstractRedisService(RedisConfigFactory redisConfigFactory) {
        this.redisConfigFactory = redisConfigFactory;
    }

    @Override
    public void clearData(RedisBase redisBase) {
        getRedisTemplate(redisBase).delete(redisBase.getKey());
    }

    protected Object innerConvertJsonNode(Object originValue, JavaType javaType) {
        if (javaType == null) {
            return String.valueOf(originValue);
        }
        if (!(originValue instanceof LinkedHashMap)) {
            throw new RuntimeException("Can not case " + originValue + " to inner object");
        }
        return JsonUtils.deserialize(originValue, javaType);
    }

    protected JavaType getJavaType(RedisBase redisBase) {
        String clazz = redisBase.getClazz();
        Map<String, JavaType> jsonNodeConvertMap = redisConfigFactory.getJsonNodeConvertMap();
        if (StringUtils.hasText(clazz) && jsonNodeConvertMap.containsKey(clazz)) {
            return jsonNodeConvertMap.get(clazz);
        }
        return null;
    }

    protected RedisTemplate<String,Object> getRedisTemplate(RedisBase redisBase){
        return redisConfigFactory.getRedisTemplate(redisBase.getJsName());
    }
}
