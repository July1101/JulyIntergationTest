package com.thg.redis.service;

import com.fasterxml.jackson.databind.JavaType;
import com.thg.redis.config.RedisConfigFactory;
import com.thg.redis.model.RedisBase;
import com.thg.redis.model.RedisDataType;
import com.thg.utils.AssertUtils;
import java.util.List;
import java.util.Set;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/6/24 21:54
 **/
@Service
@ConditionalOnBean(RedisConfigFactory.class)
public class SetRedisServiceImpl extends AbstractRedisService {

    protected SetRedisServiceImpl(RedisConfigFactory redisConfigFactory) {
        super(redisConfigFactory);
    }

    @Override
    public boolean hit(RedisBase redisBase) {
        return redisBase.getSet() != null;
    }

    @Override
    public void initializeRedisBase(RedisBase redisBase) {
        redisBase.setType(RedisDataType.SET);
        JavaType javaType = getJavaType(redisBase);
        redisBase.getList()
            .replaceAll(value -> innerConvertJsonNode(value, javaType));
    }

    @Override
    public void insertData(RedisBase redisBase) {
        getRedisTemplate(redisBase).opsForSet().add(redisBase.getKey(),redisBase.getSet());
    }

    @Override
    public void checkData(RedisBase redisBase) {
        Set<Object> actual = getRedisTemplate(redisBase).opsForSet().members(redisBase.getKey());
        List<Object> expect = redisBase.getSet();
        AssertUtils.assertEquals(expect,actual,true);
    }
}
