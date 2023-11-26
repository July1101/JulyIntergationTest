package com.thg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/3/12 21:02
 **/
@SpringBootApplication(scanBasePackages = {"com.thg"})
public class IntegrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(IntegrationApplication.class,args);
    }
}
