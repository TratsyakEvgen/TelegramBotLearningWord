package com.tratsiak.telegram.bot.mvc.lib.core.session.impl;

import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import com.tratsiak.telegram.bot.mvc.lib.core.session.SessionInitializer;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DefaultSessionInitializer implements SessionInitializer {
    @Override
    public Optional<Session> init(long id) {
        return Optional.of(new Session(id));
    }
}
