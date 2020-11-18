package com.spring.loadbean;

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
        AnnotationBean annotationBean = (AnnotationBean) getContext().getBean("annotationBean");
        BeanBean beanBean = (BeanBean) getContext().getBean("beanBean");
        UseFactoryBean useFactoryBean = getContext().getBean("useFactoryBean", UseFactoryBean.class);
    }

    private ClassPathXmlApplicationContext getContext() {
        return new ClassPathXmlApplicationContext("classpath:spring-main.xml");
    }
}
