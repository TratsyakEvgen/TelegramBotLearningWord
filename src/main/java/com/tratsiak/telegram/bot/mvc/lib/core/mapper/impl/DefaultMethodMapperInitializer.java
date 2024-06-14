package com.tratsiak.telegram.bot.mvc.lib.core.mapper.impl;

import com.tratsiak.telegram.bot.mvc.lib.core.mapper.MethodMapper;
import com.tratsiak.telegram.bot.mvc.lib.core.mapper.MethodMapperInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DefaultMethodMapperInitializer implements MethodMapperInitializer {

    private final ApplicationContext context;

    @Autowired
    public DefaultMethodMapperInitializer(ApplicationContext context) {
        this.context = context;
    }

    @Override
    @EventListener(classes = ContextRefreshedEvent.class)
    public void init() {
        Map<String, MethodMapper> beansOfType = context.getBeansOfType(MethodMapper.class);
        beansOfType.forEach((string, methodMapper) -> methodMapper.init(context));

    }
}
