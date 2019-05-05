package com.niulx.spring.framework.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @Date 2019-05-05 10:45
 * @Created by nlx
 *
 */
public class JdkDynamicAopProxy implements AopProxy,InvocationHandler {

    private final AdvisedSupport advised;

    public JdkDynamicAopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }

    @Override
    public Object getProxy() {
        return getProxy(advised.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader,advised.getTargetClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Object> interceptionAdvice = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, advised.getTargetClass());
        ReflectiveMethodInvocation methodInvocation = new ReflectiveMethodInvocation(proxy, advised.getTarget(), method, args, advised.getTargetClass(), interceptionAdvice);
        return methodInvocation.proceed();
    }
}
