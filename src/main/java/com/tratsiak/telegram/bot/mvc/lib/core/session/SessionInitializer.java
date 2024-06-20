package com.tratsiak.telegram.bot.mvc.lib.core.session;

import java.util.Optional;

public interface SessionInitializer {
    Optional<Session> init(long id);
}
