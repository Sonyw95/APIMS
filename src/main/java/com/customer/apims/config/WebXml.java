package com.customer.apims.config;

import ch.qos.logback.classic.helpers.MDCInsertingServletFilter;
import org.apache.commons.lang.StringUtils;
import org.ezdevgroup.ezframework.web.GlobalProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.util.EnumSet;

/**
 * @author Son
 */
public class WebXml implements WebApplicationInitializer {

    private static Logger log = LoggerFactory.getLogger(WebApplicationInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(ApplicationContext.class);
        rootContext.refresh();


        // Tomcat Instance env.bat 선언
        String jvmRoute = System.getProperty("jvmRoute");

        GlobalProperties globalProperties = rootContext.getBean("globalProperties", GlobalProperties.class);
        String serverType = StringUtils.defaultString(globalProperties.getProperty("server.type"), "local");
        log.debug("=========== ServerType : " + serverType);


        // Request ( pattern /* ) -> UTF-8 Encoding
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        FilterRegistration.Dynamic characterEncoding = servletContext.addFilter("characterEncodingFilter", characterEncodingFilter);
        characterEncoding.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");

        HiddenHttpMethodFilter hiddenHttpMethodFilter = new HiddenHttpMethodFilter();
        FilterRegistration.Dynamic hiddenHttpMethod = servletContext.addFilter("hiddenHttpMethodFilter",hiddenHttpMethodFilter);
        hiddenHttpMethod.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");


        // Log ( pattern /* ) -> Write Log
        MDCInsertingServletFilter insertingServletFilter = new MDCInsertingServletFilter();
        FilterRegistration.Dynamic logServletFilter = servletContext.addFilter("insertingServletFilter", insertingServletFilter);
        logServletFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");


        // Spring Listener Setting ( Spring Core Set )
        servletContext.addListener(new ContextLoaderListener(rootContext));


        // Servlet-context Setting -> *.xml 파일로 세팅하던 방식을 webxml.java 로 settng 하여 dispatcher 를 잡음.
        XmlWebApplicationContext xmlWebApplicationContext = new XmlWebApplicationContext();
       // xmlWebApplicationContext.setConfigLocation("");
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(xmlWebApplicationContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        RequestContextListener requestContextListener = new RequestContextListener();
        servletContext.addListener(requestContextListener);



    }


}
