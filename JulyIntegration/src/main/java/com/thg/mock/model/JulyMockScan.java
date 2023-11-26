package com.thg.mock.model;

import com.thg.mock.config.JulyMockScannerRegistrar;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/6/1 21:50
 **/

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(JulyMockScannerRegistrar.class)
public @interface JulyMockScan {
    Class<?>[] basePackageClasses() default {};
}
