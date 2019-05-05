package com.niulx.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * @Date 2019-05-03 22:37
 * @Created by nlx
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {

    String value() default "";

    boolean required() default true;
}
