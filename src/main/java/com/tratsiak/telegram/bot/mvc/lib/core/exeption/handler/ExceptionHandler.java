package com.tratsiak.telegram.bot.mvc.lib.core.exeption.handler;

import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;

public interface ExceptionHandler<T extends Exception> {
    BotView handle(Exception exception, Session session);
}
