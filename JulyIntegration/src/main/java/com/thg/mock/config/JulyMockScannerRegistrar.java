package com.thg.mock.config;

import com.thg.mock.model.JulyMockScan;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/6/1 21:51
 **/

@Component
public class JulyMockScannerRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
        BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        AnnotationAttributes mockScanAttrs = AnnotationAttributes
            .fromMap(importingClassMetadata.getAnnotationAttributes(JulyMockScan.class.getName()));
        List<Class<?>> classList = Arrays.stream(
            mockScanAttrs.getClassArray("basePackageClasses")).collect(Collectors.toList());
        for (Class<?> clazz : classList) {
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(clazz);
            registry.registerBeanDefinition(buildMockClassBeanName(clazz),beanDefinitionBuilder.getBeanDefinition());
        }
    }

    private String buildMockClassBeanName(Class<?> clazz) {
        return clazz.getSimpleName() + "$JulyMockBean";
    }
}
