package com.tratsiak.telegram.bot.mvc.lib.core.dispatcher.impl;

import com.tratsiak.telegram.bot.mvc.lib.core.dispatcher.DispatcherRequests;
import com.tratsiak.telegram.bot.mvc.lib.core.dispatcher.DispatchersRequestsInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DefaultDispatchersRequestsInitializer implements DispatchersRequestsInitializer {

    private final ApplicationContext context;
    private Map<String, DispatcherRequests> dispatcherRequestsMap;

    @Autowired
    public DefaultDispatchersRequestsInitializer(ApplicationContext context) {
        this.context = context;
    }

    @Override
    @EventListener(classes = ContextRefreshedEvent.class)
    public void init() {
        dispatcherRequestsMap = context.getBeansOfType(DispatcherRequests.class);
        dispatcherRequestsMap.forEach((string, dispatcherRequests) -> dispatcherRequests.init());

    }

    @Override
    public Map<String, DispatcherRequests> getDispatcherRequestsMap() {
        return dispatcherRequestsMap;
    }
}
