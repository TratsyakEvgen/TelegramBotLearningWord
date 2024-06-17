package com.tratsiak.telegram.bot.mvc.lib.core.mapper.impl;

import com.tratsiak.telegram.bot.mvc.lib.core.mapper.MethodMapper;
import com.tratsiak.telegram.bot.mvc.lib.core.mapper.MethodMapperException;
import com.tratsiak.telegram.bot.mvc.lib.core.mapper.MethodMapperInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DefaultMethodMapperInitializer implements MethodMapperInitializer {

    private final ApplicationContext context;
    private Map<String, MethodMapper> methodMappers;

    @Autowired
    public DefaultMethodMapperInitializer(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void init() {
        methodMappers = context.getBeansOfType(MethodMapper.class);
        methodMappers.forEach((string, methodMapper) -> {
            try {
                methodMapper.init(context);
            } catch (MethodMapperException e) {
                throw new RuntimeException("Can't init method mapper", e);
            }
        });

    }

    @Override
    public Map<String, MethodMapper> getMethodMappers() {
        return methodMappers;
    }
}
