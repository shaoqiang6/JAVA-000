package com.spring.loadbean;

import java.util.concurrent.TimeUnit;

/**
 * @author yansq
 * @date 2020/11/17
 */
public class UseFactoryBean {
    private int name;
    public UseFactoryBean() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1L);
        System.out.println("UseFactoryBean init");
    }
}
