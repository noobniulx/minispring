package com.niulx.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * @Date 2019-05-03 22:38
 * @Created by nlx
 */

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

    String value() default "";
}
