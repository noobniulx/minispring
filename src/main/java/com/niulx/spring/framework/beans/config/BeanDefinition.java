package com.niulx.spring.framework.beans.config;

import lombok.Data;

/**
 * @Date 2019-05-03 22:19
 * @Created by nlx
 */
@Data
public class BeanDefinition {
    private String beanClassName;
    private boolean lazyInit = false;
    private String factoryBeanName;
    private boolean isSingleton = true;
}
