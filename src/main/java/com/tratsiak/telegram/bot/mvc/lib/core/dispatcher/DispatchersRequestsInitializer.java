package com.tratsiak.telegram.bot.mvc.lib.core.dispatcher;


import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.Map;

public interface DispatchersRequestsInitializer {
    @EventListener(classes = ContextRefreshedEvent.class)
    void init();

    Map<String, DispatcherRequests> getDispatcherRequestsMap();
}
