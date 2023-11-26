package com.thg.datasource;

import com.thg.datasource.config.DataSourceAutoConfiguration1;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by tanhuigen
 * Date 2022-09-03
 * Description
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({DataSourceAutoConfiguration1.class})
public @interface EnableDataSourceAutoConfiguration {
}
