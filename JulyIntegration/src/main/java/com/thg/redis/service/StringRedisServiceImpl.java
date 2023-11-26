package com.thg.redis.service;

import com.fasterxml.jackson.databind.JavaType;
import com.thg.redis.config.RedisConfigFactory;
import com.thg.redis.model.RedisBase;
import org.junit.Assert;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/6/24 21:28
 **/
@Service
@ConditionalOnBean(RedisConfigFactory.class)
public class StringRedisServiceImpl extends AbstractRedisService {

    protected StringRedisServiceImpl(
        RedisConfigFactory redisConfigFactory) {
        super(redisConfigFactory);
    }

    @Override
    public boolean hit(RedisBase redisBase) {
        return redisBase.getValue() != null;
    }

    @Override
    public void initializeRedisBase(RedisBase redisBase) {
        Object beforeValue = redisBase.getValue();
        JavaType javaType = getJavaType(redisBase);
        Object afterValue = innerConvertJsonNode(beforeValue,javaType);
        redisBase.setValue(afterValue);
    }

    @Override
    public void insertData(RedisBase redisBase) {
        getRedisTemplate(redisBase).opsForValue().set(redisBase.getKey(),redisBase.getValue());
    }

    @Override
    public void checkData(RedisBase redisBase) {
        Object actual = getRedisTemplate(redisBase).opsForValue().get(redisBase.getKey());
        Object expect = redisBase.getValue();
        Assert.assertEquals(expect,actual);
    }
}
