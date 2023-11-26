package com.thg.redis.config;

import com.thg.config.postprocessor.PostProcessService;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/7/6 19:38
 **/
public class RedisTemplatePostProcessServiceImpl implements PostProcessService {

    @Override
    public boolean hit(Object bean, String beanName) {
        return bean instanceof RedisTemplate ;
    }

    @Override
    public Object beanProcess(Object bean, String beanName) {
        return RedisTemplateCglibProxy.newInstance((RedisTemplate<String, Object>) bean);
    }
}
