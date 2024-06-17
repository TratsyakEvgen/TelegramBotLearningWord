package com.tratsiak.telegram.bot.mvc.lib.core.exeption.handler.impl;

import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.lib.core.exeption.handler.ErrorViewer;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
@Component
public class DefaultErrorViewer implements ErrorViewer {
    @Override
    public BotView getDefaultError(Session session) {
        return new BotView(SendMessage.builder().chatId(session.getId()).text("Error!!!").build());
    }
}
