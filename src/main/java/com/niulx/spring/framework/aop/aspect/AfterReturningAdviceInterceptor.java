package com.niulx.spring.framework.aop.aspect;

import com.niulx.spring.framework.aop.MethodInterceptor;
import com.niulx.spring.framework.aop.ReflectiveMethodInvocation;

import java.lang.reflect.Method;

/**
 * @Date 2019-05-05 14:43
 * @Created by nlx
 */
public class AfterReturningAdviceInterceptor  extends  AbstractAspectAdvice implements MethodInterceptor {

    private JoinPoint joinPoint;

    public AfterReturningAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    private void afterReturning(Object retVal, Method method, Object[] arguments, Object aThis) throws Throwable {
        super.invokeAdviceMethod(this.joinPoint,retVal,null);
    }

    @Override
    public Object invoke(ReflectiveMethodInvocation invocation) throws Throwable {
        Object val = invocation.proceed();
        joinPoint = invocation;
        afterReturning(val,joinPoint.getMethod(),joinPoint.getArguments(),joinPoint.getThis());
        return val;
    }
}
