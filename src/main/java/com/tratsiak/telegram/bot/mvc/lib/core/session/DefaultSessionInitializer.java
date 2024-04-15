package com.tratsiak.telegram.bot.mvc.lib.core.session;

import org.springframework.stereotype.Component;

@Component
public class DefaultSessionInitializer implements SessionInitializer {
    @Override
    public Session init(long id) {
        return new Session(id);
    }
}
