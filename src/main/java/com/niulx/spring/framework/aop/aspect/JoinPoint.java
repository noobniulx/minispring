package com.niulx.spring.framework.aop.aspect;

import java.lang.reflect.Method;

/**
 * @Date 2019-05-05 16:36
 * @Created by nlx
 */
public interface JoinPoint {

    Object getThis();

    Object[] getArguments();

    Method getMethod();

    void setUserAttribute(String key, Object value);

    Object getUserAttribute(String key);
}
