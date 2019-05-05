package com.niulx.spring.framework.beans.support;

import com.niulx.spring.framework.beans.config.BeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Date 2019-05-03 22:49
 * @Created by nlx
 */
public class BeanDefinitionReader {

    public Properties properties = new Properties();
    private List<String> registyBeanClasses = new ArrayList<>();

    private final String SCAN_PACKAGE = "scanPackage";

    public BeanDefinitionReader(String...locations) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:", ""));
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        doScanner(properties.getProperty(SCAN_PACKAGE));
    }

    private void doScanner(String scanPackage) {
        URL url = this.getClass().getResource("/" + scanPackage.replaceAll("\\.","/"));
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()) {
            if(file.isDirectory()){
                doScanner(scanPackage + "." + file.getName());
            }else{
                if(!file.getName().endsWith(".class")){ continue;}
                String className = (scanPackage + "." + file.getName().replace(".class",""));
                registyBeanClasses.add(className);
            }
        }
    }


    public List<BeanDefinition> loadBeanDefinitions() {
        List<BeanDefinition> res = new ArrayList<>();
        for (String beanName : registyBeanClasses) {
            try {
                Class<?> className = Class.forName(beanName);
                if(className.isInterface()) { continue; }
                res.add(createBeanDefinition(toLowerFirstCase(className.getSimpleName()),className.getName()));
                Class<?>[] interfaces = className.getInterfaces();
                for (Class<?> i : interfaces) {
                    res.add(createBeanDefinition(i.getName(),className.getName()));
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    private BeanDefinition createBeanDefinition(String factoryBeanName,String beanClassName) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }

    private String toLowerFirstCase(String simpleName) {
        char [] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
