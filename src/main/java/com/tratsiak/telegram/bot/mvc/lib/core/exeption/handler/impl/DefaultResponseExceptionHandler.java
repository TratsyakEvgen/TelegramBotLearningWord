package com.tratsiak.telegram.bot.mvc.lib.core.exeption.handler.impl;

import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.lib.core.exeption.handler.ExceptionHandler;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import com.tratsiak.telegram.bot.mvc.lib.exception.ResponseException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class DefaultResponseExceptionHandler implements ExceptionHandler<ResponseException> {
    @Override
    public BotView handle(Exception exception, Session session) {
        return new BotView(SendMessage.builder().chatId(session.getId()).text(exception.getMessage()).build());
    }
}
