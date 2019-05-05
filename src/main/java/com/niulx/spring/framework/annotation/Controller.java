package com.niulx.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * @Date 2019-05-03 22:34
 * @Created by nlx
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {

    String value() default "";
}
