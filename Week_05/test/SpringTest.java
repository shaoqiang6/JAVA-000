package com.shawn.spring;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author yansq
 * @date 2020/11/17
 */
public class SpringTest {

    @Test
    public void xmlLoadTest() {
        XmlBean xmlBean = (XmlBean) getContext().getBean("xmlBean");
    }

    @Test
    public void annotationLoadTest() {
        AnnotationBean xmlBean = (AnnotationBean) getContext().getBean("annotationBean");
    }

    @Test
    public void annotationLoadTest2() {
        BeanBean xmlBean = (BeanBean) getContext().getBean("beanBean");
    }

    @Test
    public void factoryLoadTest() {
        UseFactoryBean xmlBean = getContext().getBean("useFactoryBean", UseFactoryBean.class);
    }

    private ClassPathXmlApplicationContext getContext() {
        return new ClassPathXmlApplicationContext("classpath:spring/spring-main.xml");
    }
}
