package com.niulx.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * @Date 2019-05-03 22:36
 * @Created by nlx
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
    String value() default "";
}
