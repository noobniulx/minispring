package com.niulx.spring.framework.beans.config;

/**
 * @Date 2019-05-04 15:28
 * @Created by nlx
 */
public class BeanPostProcessor {

    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }

}
