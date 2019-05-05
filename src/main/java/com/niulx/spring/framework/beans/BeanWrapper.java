package com.niulx.spring.framework.beans;

/**
 * @Date 2019-05-04 00:32
 * @Created by nlx
 */
public class BeanWrapper {

    private Object wrappedInstance;
    private Class<?> wrappedClass;

    public BeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
    }

    public Object getWrappedInstance() {
        return wrappedInstance;
    };

    public Class<?> getWrappedClass(){
        return wrappedInstance.getClass();
    };

}
