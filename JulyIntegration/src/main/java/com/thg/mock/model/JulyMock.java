package com.thg.mock.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/5/21 16:46
 **/


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface JulyMock {

}
