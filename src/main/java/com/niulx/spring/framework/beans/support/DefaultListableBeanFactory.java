package com.niulx.spring.framework.beans.support;

import com.niulx.spring.framework.beans.config.BeanDefinition;
import com.niulx.spring.framework.context.support.AbstractApplicatonContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date 2019-05-03 22:16
 * @Created by nlx
 */
public class DefaultListableBeanFactory  extends AbstractApplicatonContext {

    public final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

}
