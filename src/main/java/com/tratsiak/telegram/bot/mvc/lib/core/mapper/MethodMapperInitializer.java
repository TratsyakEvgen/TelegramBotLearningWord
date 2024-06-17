package com.tratsiak.telegram.bot.mvc.lib.core.mapper;


import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.Map;

public interface MethodMapperInitializer {
    @EventListener(classes = ContextRefreshedEvent.class)
    void init();
    Map<String, MethodMapper> getMethodMappers();
}
