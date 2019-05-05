package com.niulx.spring.framework.webmvc.servlet;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @Date 2019-05-04 17:24
 * @Created by nlx
 */
@Data
public class HandlerMapping {

    private Object controller;
    private Method method;
    private Pattern pattern;

    public HandlerMapping(Object controller, Method method, Pattern pattern) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
    }
}
