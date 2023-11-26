package com.thg.mock.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/5/21 20:53
 **/

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface UnMockMethod {
    String id();
}
