package com.tratsiak.telegram.bot.mvc.lib.core.session;

public interface SessionModifier {
    void modify(Session session) throws Exception;
}
