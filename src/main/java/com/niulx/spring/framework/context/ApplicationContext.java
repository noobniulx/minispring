package com.niulx.spring.framework.context;

import com.niulx.spring.framework.annotation.Autowired;
import com.niulx.spring.framework.annotation.Controller;
import com.niulx.spring.framework.annotation.Service;
import com.niulx.spring.framework.aop.AdvisedSupport;
import com.niulx.spring.framework.aop.AopProxy;
import com.niulx.spring.framework.aop.CglibAopProxy;
import com.niulx.spring.framework.aop.JdkDynamicAopProxy;
import com.niulx.spring.framework.aop.config.AopConfig;
import com.niulx.spring.framework.beans.BeanWrapper;
import com.niulx.spring.framework.beans.config.BeanDefinition;
import com.niulx.spring.framework.beans.config.BeanPostProcessor;
import com.niulx.spring.framework.beans.support.BeanDefinitionReader;
import com.niulx.spring.framework.beans.support.DefaultListableBeanFactory;
import com.niulx.spring.framework.beans.BeanFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date 2019-05-03 22:11
 * @Created by nlx
 */
public class ApplicationContext extends DefaultListableBeanFactory implements BeanFactory {

    private String[] configLocationss;

    public BeanDefinitionReader reader;

    private Map<String,BeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<String, BeanWrapper>();

    public ApplicationContext(String... configLocations) {
        this.configLocationss = configLocations;
        refresh();
    }

    @Override
    public Object getBean(String beanName) throws Exception {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        BeanPostProcessor postProcessor  = new BeanPostProcessor();
        postProcessor.postProcessBeforeInitialization(beanDefinition,beanName);
        // init
        Object instance = initializeBean(beanName, beanDefinition);
        BeanWrapper wrapper = new BeanWrapper(instance);
        factoryBeanInstanceCache.put(beanName, wrapper);
        postProcessor.postProcessAfterInitialization(instance,beanName);
        // autowired
        populateBean(beanName,beanDefinition,wrapper);

        // BeanPostProcessor  ---> aop

        return factoryBeanInstanceCache.get(beanName).getWrappedInstance();
    }

    private Object initializeBean(String beanName, BeanDefinition beanDefinition) {
        String className = beanDefinition.getBeanClassName();
        Object instance = null;
        try {
            Class<?> beanClass = Class.forName(className);
            instance = beanClass.newInstance();
            AdvisedSupport advisedSupport = initAopConfig();
            advisedSupport.setTargetClass(beanClass);
            advisedSupport.setTarget(instance);
            if(advisedSupport.pointCutMatch()) {
               instance = createProxy(advisedSupport).getProxy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

    private AopProxy createProxy(AdvisedSupport advisedSupport) {
        Class<?> targetClass = advisedSupport.getTargetClass();
        if(targetClass.getInterfaces().length >= 1) {
            return new JdkDynamicAopProxy(advisedSupport);
        }
        return new CglibAopProxy(advisedSupport);
    }

    private AdvisedSupport initAopConfig() {
        AopConfig aopConfig = new AopConfig();
        aopConfig.setPointCut(reader.properties.getProperty("pointCut"));
        aopConfig.setAspectClass(reader.properties.getProperty("aspectClass"));
        aopConfig.setAspectBefore(reader.properties.getProperty("aspectBefore"));
        aopConfig.setAspectAfter(reader.properties.getProperty("aspectAfter"));
        aopConfig.setAspectAfterThrow(reader.properties.getProperty("aspectAfterThrow"));
        aopConfig.setAspectAfterThrowingName(reader.properties.getProperty("aspectAfterThrowingName"));
        return new AdvisedSupport(aopConfig);
    }

    private void populateBean(String beanName, BeanDefinition beanDefinition, BeanWrapper wrapper) {
        Object instance = wrapper.getWrappedInstance();
        Class<?> instanceClass = wrapper.getWrappedClass();
        if(!(instanceClass.isAnnotationPresent(Controller.class) || instanceClass.isAnnotationPresent(Service.class))) {return;};

        Field[] fields = instanceClass.getDeclaredFields();
        for(Field field : fields) {
            if(!field.isAnnotationPresent(Autowired.class)) {continue;}
            Autowired annotation = field.getAnnotation(Autowired.class);
            String value = annotation.value().trim();
            if("".equals(value)) {
                value = field.getType().getName();
            }
            field.setAccessible(true);
            try {
                if(factoryBeanInstanceCache.get(value) == null) { continue; }
                field.set(instance,factoryBeanInstanceCache.get(value).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void refresh()  {
        reader = new BeanDefinitionReader(configLocationss);
        List<BeanDefinition> beanDefinitions = reader.loadBeanDefinitions();
        doRegisterBeanDefinitions(beanDefinitions);
        doAutoWired();
    }


    public Properties getConfig() {
        return reader.properties;
    }

    private void doAutoWired() {
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry: super.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            if(!beanDefinitionEntry.getValue().isLazyInit()) {
                try {
                    getBean(beanName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doRegisterBeanDefinitions(List<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            if(!beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(),beanDefinition);
            }
        }
    }


    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }
}
