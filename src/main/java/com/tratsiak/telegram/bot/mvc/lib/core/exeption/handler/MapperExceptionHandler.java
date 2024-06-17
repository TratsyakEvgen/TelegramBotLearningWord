package com.tratsiak.telegram.bot.mvc.lib.core.exeption.handler;

import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

public interface MapperExceptionHandler {
    @EventListener(classes = ContextRefreshedEvent.class)
    void init();

    BotView handle(Exception e, Session session);
}
