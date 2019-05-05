package com.niulx.spring.framework.aop;

/**
 * @Date 2019-05-05 11:04
 * @Created by nlx
 */
public interface MethodInterceptor {
    Object invoke(ReflectiveMethodInvocation invocation) throws Throwable;
}
