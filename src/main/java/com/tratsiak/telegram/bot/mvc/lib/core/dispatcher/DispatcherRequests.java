package com.tratsiak.telegram.bot.mvc.lib.core.dispatcher;

import com.tratsiak.telegram.bot.mvc.lib.core.View;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;

import java.util.Optional;

public interface DispatcherRequests {
    Optional<View> executeMethod(Session session);

    void init();
}
