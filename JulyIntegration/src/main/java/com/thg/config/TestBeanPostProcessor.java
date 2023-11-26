package com.thg.config;

import com.thg.config.postprocessor.PostProcessService;
import com.thg.config.postprocessor.StrategyServiceFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/5/27 16:32
 **/

@Component
public class TestBeanPostProcessor implements BeanPostProcessor {

    private final StrategyServiceFactory strategyServiceFactory;

    public TestBeanPostProcessor() {
        this.strategyServiceFactory = new StrategyServiceFactory();
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
        throws BeansException {
        PostProcessService postProcessService = strategyServiceFactory.selectService(bean, beanName);
        if (postProcessService != null) {
            bean = postProcessService.beanProcess(bean, beanName);
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
