package com.niulx.spring.framework.aop;

import com.niulx.spring.framework.aop.aspect.AfterReturningAdviceInterceptor;
import com.niulx.spring.framework.aop.aspect.MethodBeforeAdviceInterceptor;
import com.niulx.spring.framework.aop.aspect.ThrowsAdviceInterceptor;
import com.niulx.spring.framework.aop.config.AopConfig;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Date 2019-05-05 10:48
 * @Created by nlx
 */
@Data
public class AdvisedSupport {

    private Pattern pattern;

    private Class<?> targetClass;
    private Object target;

    private AopConfig config;

    private transient Map<Method, List<Object>> methodCache;

    public AdvisedSupport(AopConfig config) {
        this.config = config;
    }

    public Class<?> getTargetClass() {
        return this.targetClass;
    }

    public Object getTarget() {
        return this.target;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public void setTarget(Object target) {
        this.target = target;
        parse();
    }


    private void parse() {
        String pointCut = config.getPointCut()
                .replaceAll("\\.","\\\\.")
                .replaceAll("\\\\.\\*",".*")
                .replaceAll("\\(","\\\\(")
                .replaceAll("\\)","\\\\)");

        // pointCut=public .* demo.service..*Service..*(.*)
        String pointCutForClassRegex = pointCut.substring(0,pointCut.lastIndexOf("\\(") - 4);
        pattern = Pattern.compile("class " + pointCutForClassRegex.substring(
                pointCutForClassRegex.lastIndexOf(" ") + 1));

        methodCache = new HashMap<Method, List<Object>>();
        Pattern pattern = Pattern.compile(pointCut);
        try {
            Class<?> name = Class.forName(config.getAspectClass());
            Map<String,Method> aspectMethods = new HashMap<>();
            for(Method m :name.getMethods()) {
                aspectMethods.put(m.getName(), m);
            }

            for(Method m : targetClass.getMethods()) {
                String methodString = m.toString();
                if (methodString.contains("throws")) {
                    methodString = methodString.substring(0, methodString.lastIndexOf("throws")).trim();
                }
                Matcher matcher = pattern.matcher(methodString);
                if(matcher.matches()) {
                    List<Object> advices = new LinkedList<Object>();
                    if(null != config.getAspectBefore() || !"".equals(config.getAspectBefore())) {
                        advices.add(new MethodBeforeAdviceInterceptor(aspectMethods.get(config.getAspectBefore()), name.newInstance()));
                    }
                    if(null != config.getAspectAfter() || !"".equals(config.getAspectAfter())) {
                        advices.add(new AfterReturningAdviceInterceptor(aspectMethods.get(config.getAspectAfter()),name.newInstance()));
                    }
                    if(null != config.getAspectAfterThrow() || !"".equals(config.getAspectAfterThrow())) {
                        ThrowsAdviceInterceptor throwsAdviceInterceptor = new ThrowsAdviceInterceptor(aspectMethods.get(config.getAspectAfterThrow()), name.newInstance());
                        throwsAdviceInterceptor.setThrowName(config.getAspectAfterThrowingName());
                        advices.add(throwsAdviceInterceptor);
                    }
                    methodCache.put(m,advices);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) {
        List<Object> cached = methodCache.get(method);
        if(cached == null) {
            Method m = null;
            try {
                m = targetClass.getMethod(method.getName(), method.getParameterTypes());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            cached = methodCache.get(m);
            methodCache.put(m,cached);
        }
        return cached;
    }


    public boolean pointCutMatch() {
        return pattern.matcher(this.targetClass.toString()).matches();
    }

}
