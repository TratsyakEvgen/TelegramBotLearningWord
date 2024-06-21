package com.tratsiak.telegram.bot.mvc.lib.core.session;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotSessions {
    Session getSession(Update update);
}
