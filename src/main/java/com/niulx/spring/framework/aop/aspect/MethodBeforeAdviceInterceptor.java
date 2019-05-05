package com.niulx.spring.framework.aop.aspect;

import com.niulx.spring.framework.aop.MethodInterceptor;
import com.niulx.spring.framework.aop.ReflectiveMethodInvocation;

import java.lang.reflect.Method;

/**
 * @Date 2019-05-05 14:43
 * @Created by nlx
 */
public class MethodBeforeAdviceInterceptor extends AbstractAspectAdvice implements MethodInterceptor {

    private JoinPoint joinPoint;

    public MethodBeforeAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    private void before(Method method,Object[] args,Object target) throws Throwable {
        super.invokeAdviceMethod(joinPoint,null,null);
    }

    @Override
    public Object invoke(ReflectiveMethodInvocation invocation) throws Throwable {
        joinPoint = invocation;
        before(invocation.getMethod(), invocation.getArguments(),invocation.getThis());
        return invocation.proceed();
    }
}
