package com.spring.loadbean;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author yansq
 * @date 2020/11/17
 */
public class FactoryBeanImpl implements FactoryBean<UseFactoryBean> {
    @Override
    public UseFactoryBean getObject() throws Exception {
        return new UseFactoryBean();
    }

    @Override
    public Class<?> getObjectType() {
        return UseFactoryBean.class;
    }
}
