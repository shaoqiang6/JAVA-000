package com.spring.loadbean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yansq
 * @date 2020/11/17
 */
@Configuration
public class ConfigBean {

    @Bean(name = "beanBean")
    public BeanBean getBeanBean() {
        return new BeanBean();
    }
}
