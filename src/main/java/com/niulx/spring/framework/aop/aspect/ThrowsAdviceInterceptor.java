package com.niulx.spring.framework.aop.aspect;

import com.niulx.spring.framework.aop.MethodInterceptor;
import com.niulx.spring.framework.aop.ReflectiveMethodInvocation;

import java.lang.reflect.Method;


/**
 * @Date 2019-05-05 14:44
 * @Created by nlx
 */
public class ThrowsAdviceInterceptor extends AbstractAspectAdvice implements MethodInterceptor {

    private String throwName;

    public ThrowsAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    public void setThrowName(String throwName) {
        this.throwName = throwName;
    }

    @Override
    public Object invoke(ReflectiveMethodInvocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        }catch (Throwable e){
            invokeAdviceMethod(invocation,null,e.getCause());
            throw e;
        }
    }
}
