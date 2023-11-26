package com.thg.mock.config;

import com.thg.TestContext;
import com.thg.mock.model.JulyMock;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/5/29 21:17
 **/
@Slf4j
@Component
public class MockBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    private ConfigurableListableBeanFactory beanFactory;
    private BeanDefinitionRegistry registry;


    @Override
    public void postProcessBeanFactory(
        ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        this.registry = (BeanDefinitionRegistry) beanFactory;
        Arrays.stream(beanFactory.getBeanNamesForAnnotation(JulyMock.class))
            .forEach(this::modifyMockBeanDefinition);
    }

    private void modifyMockBeanDefinition(String beanName){
        Class<?> type = beanFactory.getType(beanName);
        Map<String,Object> beanMap = new HashMap<>();
        ReflectionUtils.doWithFields(type,(field)->{

        });

        Arrays.stream(type.getDeclaredFields())
            .filter(x -> x.getAnnotation(MockBean.class) != null)
            .forEach(x->this.registerMockBeanWithListener(x,beanMap));
        TestContext.mockBeanMap.put(beanName,beanMap);
    }


    private void registerMockBeanWithListener(Field field, Map<String, Object> beanMap) {
        String beanName = getRealBeanNameWithField(field);
        Class<?> type = field.getType();
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
        BeanDefinition copyBeanDefinition = copyBeanDefinition(type, beanDefinition.isPrimary());
        registry.removeBeanDefinition(beanName);
        registry.registerBeanDefinition(beanName, copyBeanDefinition);
        Object mock = buildNewMockBean(type);
        beanFactory.registerSingleton(beanName, mock);
        beanMap.put(field.getName(), mock);
    }


    private String getRealBeanNameWithField(Field field){
        String name = field.getName();
        Class<?> type = field.getType();
        String[] beanNamesForType;
        if(!beanFactory.containsBeanDefinition(name)){
            try {
                beanNamesForType = beanFactory.getBeanNamesForType(type);
            } catch (Exception e) {
                log.error("There is not bean named:{} or typed:{}", name, type);
                throw new RuntimeException(e);
            }
            if (beanNamesForType.length != 1) {
                log.error("Duplicate bean with type:{}, we could choose the mock bean", type);
                throw new RuntimeException();
            }
            name = beanNamesForType[0];
        }
        return name;
    }

    private BeanDefinition copyBeanDefinition(Class<?> type, boolean isPrime){
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
            .genericBeanDefinition(type)
            .setPrimary(isPrime);
        return beanDefinitionBuilder.getBeanDefinition();
    }

    private Object buildNewMockBean(Class<?> clazz) {
        MockSettings mockSettings = Mockito.withSettings();
        mockSettings.invocationListeners(new MockitoInterceptor());
        return Mockito.mock(clazz, mockSettings);
    }
}
