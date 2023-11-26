package com.thg.redis;

import com.thg.ComponentService;
import com.thg.redis.config.RedisConfigFactory;
import com.thg.redis.model.RedisBase;
import com.thg.redis.model.RedisData;
import com.thg.redis.service.RedisService;
import java.util.List;
import java.util.function.BiConsumer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/6/17 22:33
 **/
@ConditionalOnBean(RedisConfigFactory.class)
@Component
public class RedisComponentService implements ComponentService<RedisData> {

    private static final String REDIS = "redis";

    private final List<RedisService> redisServiceList;

    public RedisComponentService(List<RedisService> redisServiceList) {
        this.redisServiceList = redisServiceList;
    }

    @Override
    public String getName() {
        return REDIS;
    }

    @Override
    public RedisData postProcessAfterDeserialize(RedisData data) {
        this.execute(RedisService::initializeRedisBase, data.getInit());
        this.execute(RedisService::initializeRedisBase, data.getCheck());
        return data;
    }

    @Override
    public void clearDataBeforeTest(RedisData data) {
        this.execute(RedisService::clearData,data.getInit());
        this.execute(RedisService::clearData,data.getCheck());
    }

    @Override
    public void initDataBeforeTest(RedisData data) {
        this.execute(RedisService::insertData,data.getInit());
    }

    @Override
    public void checkDataAfterTest(RedisData data) {
        this.execute(RedisService::checkData,data.getCheck());
    }

    @Override
    public void clearDataAfterTest(RedisData data) {
        this.execute(RedisService::clearData,data.getInit());
        this.execute(RedisService::clearData,data.getCheck());
    }


    void execute(BiConsumer<RedisService, RedisBase> biConsumer, List<RedisBase> redisBaseList) {
        if(!CollectionUtils.isEmpty(redisBaseList)){
            redisBaseList.forEach(redisBase -> execute(biConsumer,redisBase));
        }
    }

    void execute(BiConsumer<RedisService, RedisBase> biConsumer, RedisBase redisBase) {
        RedisService redisService = selectService(redisBase);
        biConsumer.accept(redisService, redisBase);
    }

    public RedisService selectService(RedisBase redisBase) {
        RedisService result = null;
        for (RedisService redisService : redisServiceList) {
            if (redisService.hit(redisBase)) {
                if (result == null) {
                    result = redisService;
                } else {
                    throw new RuntimeException("duplicate strategy when select " + redisBase);
                }
            }
        }
        if (result == null) {
            throw new RuntimeException("No strategy hit when select " + redisBase);
        }
        return result;
    }
}
