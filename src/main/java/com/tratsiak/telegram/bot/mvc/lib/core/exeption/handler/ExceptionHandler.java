package com.tratsiak.telegram.bot.mvc.lib.core.exeption.handler;

import com.tratsiak.telegram.bot.mvc.lib.core.View;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;

public interface ExceptionHandler<T extends Exception> {
    View handle(Exception exception, Session session);
}
