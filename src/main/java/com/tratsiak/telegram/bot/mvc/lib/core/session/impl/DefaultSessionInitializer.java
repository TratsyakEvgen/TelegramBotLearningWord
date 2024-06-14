package com.tratsiak.telegram.bot.mvc.lib.core.session.impl;

import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import com.tratsiak.telegram.bot.mvc.lib.core.session.SessionInitializer;
import org.springframework.stereotype.Component;

@Component
public class DefaultSessionInitializer implements SessionInitializer {
    @Override
    public Session init(long id) {
        return new Session(id);
    }
}
