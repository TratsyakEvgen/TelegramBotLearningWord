package com.tratsiak.telegram.bot.mvc.lib.core.session;

public interface SessionInitializer {
    Session init(long id) throws Exception;
}
