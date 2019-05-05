package com.niulx.spring.framework.beans;

/**
 * @Date 2019-05-03 22:04
 * @Created by nlx
 */
public interface BeanFactory {
    Object getBean(String beanName) throws Exception;
}
