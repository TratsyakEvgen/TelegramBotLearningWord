package com.tratsiak.telegram.bot.mvc.lib.core.exeption.handler;

import com.tratsiak.telegram.bot.mvc.lib.core.View;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;

import java.util.Optional;

public interface MapperExceptionHandler {
    void init();

    Optional<View> handle(Exception e, Session session);
}
