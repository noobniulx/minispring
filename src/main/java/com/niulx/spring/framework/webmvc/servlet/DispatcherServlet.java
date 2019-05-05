package com.niulx.spring.framework.webmvc.servlet;

import com.niulx.spring.framework.annotation.Controller;
import com.niulx.spring.framework.annotation.RequestMapping;
import com.niulx.spring.framework.context.ApplicationContext;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Date 2019-05-04 16:46
 * @Created by nlx
 */
@Slf4j
public class DispatcherServlet extends HttpServlet {

    private ApplicationContext applicationContext;

    private String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    private Map<HandlerMapping, HandlerAdapter> handlerAdapters = new ConcurrentHashMap<>();

    private List<ViewResolver> viewResolvers = new ArrayList<ViewResolver>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatch(req,resp);
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) {
        // get handmapping
        HandlerMapping handler = getHandler(req);
        if(handler == null) {return;}
        HandlerAdapter adapter = getHandlerAdapter(handler);
        ModelAndView mv = adapter.handle(req, resp, handler);
        // process
        processDispatchResult(req,resp,mv);
    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, ModelAndView mv) {
         if(null == mv) {return;}
         if(viewResolvers.isEmpty()) {return;}
         for (ViewResolver resolver : viewResolvers) {
             View view = resolver.resolveViewName(mv.getViewName(), null);
             try {
                 view.render(mv.getModel(),req,resp);
             } catch (Exception e) {
                 e.printStackTrace();
             }
             return;
         }
    }

    private HandlerAdapter getHandlerAdapter(HandlerMapping handler) {
        if(handlerAdapters.isEmpty()) {return null;}
        HandlerAdapter adapter = handlerAdapters.get(handler);
        if(adapter.supports(handler)) { return adapter;}
        return null;
    }

    private HandlerMapping getHandler(HttpServletRequest req) {
        if(handlerMappings.isEmpty()) {
            return null;
        }
        String requestURI = req.getRequestURI();
        String path = req.getContextPath();
        requestURI = requestURI.replace(path, "").replaceAll("/+", "/");

        for (HandlerMapping handlerMapping : handlerMappings) {
            Matcher matcher = handlerMapping.getPattern().matcher(requestURI);
            if(matcher.matches()) {
                return handlerMapping;
            }
        }
        return null;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        // init applicationcontext
        applicationContext = new ApplicationContext(config.getInitParameter(CONTEXT_CONFIG_LOCATION));
        // init
        initStrategies(applicationContext);
    }

    private void initStrategies(ApplicationContext applicationContext) {
        initMultipartResolver(applicationContext);
        initLocaleResolver(applicationContext);
        initThemeResolver(applicationContext);

        // handlermapping
        initHandlerMappings(applicationContext);
        // handleradpter
        initHandlerAdapters(applicationContext);
        initHandlerExceptionResolvers(applicationContext);
        initRequestToViewNameTranslator(applicationContext);
        // viewandmovel
        initViewResolvers(applicationContext);
        initFlashMapManager(applicationContext);

    }

    private void initViewResolvers(ApplicationContext applicationContext) {

        String templateRoot = applicationContext.getConfig().getProperty("templateRoot");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();

        File templateRootDir = new File(templateRootPath);
        String[] templates = templateRootDir.list();
        for (int i = 0; i < templates.length; i ++) {
            this.viewResolvers.add(new ViewResolver(templateRoot));
        }

    }

    private void initFlashMapManager(ApplicationContext applicationContext) {}

    private void initRequestToViewNameTranslator(ApplicationContext applicationContext) {}

    private void initHandlerExceptionResolvers(ApplicationContext applicationContext) {}

    private void initHandlerAdapters(ApplicationContext applicationContext) {
        for(HandlerMapping handlerMapping : this.handlerMappings) {
            handlerAdapters.put(handlerMapping,new HandlerAdapter());
        }
    }

    private void initHandlerMappings(ApplicationContext applicationContext) {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String bean : beanDefinitionNames) {
            try {
                Object contextBean = applicationContext.getBean(bean);
                Class<?> beanClass = contextBean.getClass();
                if(!beanClass.isAnnotationPresent(Controller.class)) {continue;}

                String baseUrl = "";
                if(beanClass.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = beanClass.getAnnotation(RequestMapping.class);
                    baseUrl = requestMapping.value().trim();
                }
                Method[] methods = beanClass.getMethods();
                for (Method method : methods) {
                    if(!method.isAnnotationPresent(RequestMapping.class)) { continue; }
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    String regex = ("/" + baseUrl + "/" + requestMapping.value().replaceAll("\\*",".*")).replaceAll("/+", "/");
                    Pattern pattern = Pattern.compile(regex);
                    handlerMappings.add(new HandlerMapping(contextBean, method, pattern));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initThemeResolver(ApplicationContext applicationContext) {}

    private void initLocaleResolver(ApplicationContext applicationContext) {}

    private void initMultipartResolver(ApplicationContext applicationContext) {}
}
