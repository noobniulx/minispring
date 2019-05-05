package com.niulx.spring.framework.aop;

/**
 * @Date 2019-05-05 10:44
 * @Created by nlx
 */
public interface AopProxy {

    Object getProxy();

    Object getProxy(ClassLoader classLoader);

}
