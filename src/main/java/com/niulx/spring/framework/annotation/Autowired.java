package com.niulx.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * @Date 2019-05-03 22:33
 * @Created by nlx
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    String value() default "";
}
