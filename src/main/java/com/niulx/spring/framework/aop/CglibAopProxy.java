package com.niulx.spring.framework.aop;

/**
 * @Date 2019-05-05 10:46
 * @Created by nlx
 */
public class CglibAopProxy implements AopProxy {

    public CglibAopProxy(AdvisedSupport advisedSupport) {}

    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}
