package com.github.lemniscate.spring.jsonviews.hooks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author dave 1/28/15 1:20 PM
 */
public class JsonViewHandlerDecorator implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger( JsonViewHandlerDecorator.class );

    @Inject
    private RequestMappingHandlerAdapter adapter;

    private List<Class<? extends AbstractMessageConverterMethodProcessor>> processorsToDecorate =
        Arrays.asList(RequestResponseBodyMethodProcessor.class, HttpEntityMethodProcessor.class);

    public JsonViewHandlerDecorator(){}

    @Inject
    public JsonViewHandlerDecorator(List<Class<? extends AbstractMessageConverterMethodProcessor>> processorsToDecorate) {
        this.processorsToDecorate = processorsToDecorate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList<HandlerMethodReturnValueHandler>(adapter.getReturnValueHandlers());
        decorateHandlers(handlers);
        adapter.setReturnValueHandlers(handlers);
    }
    private void decorateHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        for (HandlerMethodReturnValueHandler handler : handlers) {
            if ( processorsToDecorate.contains(handler.getClass()) ) {
                ViewInjectingReturnValueHandler decorator = new ViewInjectingReturnValueHandler(handler);
                int index = handlers.indexOf(handler);
                handlers.set(index, decorator);
                log.info("JsonView processor decorator wired up for " + handler.getClass().getSimpleName());
            }
        }
    }
}
