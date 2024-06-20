package com.tratsiak.telegram.bot.mvc.lib.core.dispatcher;

import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import org.springframework.context.ApplicationContext;

public interface DispatcherRequests {

    BotView executeMethod(String path, Session session) throws ExecuteMethodDispatcherRequestsException;

    void init(ApplicationContext context);
}
