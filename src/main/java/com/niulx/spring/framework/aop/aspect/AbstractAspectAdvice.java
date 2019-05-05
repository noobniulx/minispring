package com.niulx.spring.framework.aop.aspect;

import java.lang.reflect.Method;

/**
 * @Date 2019-05-05 15:02
 * @Created by nlx
 */
public abstract class AbstractAspectAdvice {

    private Method aspectMethod;
    private Object aspectTarget;

    public AbstractAspectAdvice(Method aspectMethod, Object aspectTarget) {
        this.aspectMethod = aspectMethod;
        this.aspectTarget = aspectTarget;
    }

    public Object invokeAdviceMethod(JoinPoint joinPoint, Object returnValue, Throwable t) throws Throwable{
        Class<?>[] types = aspectMethod.getParameterTypes();
        if(null == types || types.length == 0) {
            return aspectMethod.invoke(aspectTarget);
        }
        Object[] args = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            if(types[i] == JoinPoint.class) {
                args[i] = joinPoint;
            } else if (types[i] == Throwable.class) {
                args[i] = t;
            } else if(types[i] == Object.class) {
                args[i] = returnValue;
            }
        }
        return aspectMethod.invoke(aspectTarget,args);
    }
}
