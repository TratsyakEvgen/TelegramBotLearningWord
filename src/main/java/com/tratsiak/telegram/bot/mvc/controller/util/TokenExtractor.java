package com.tratsiak.telegram.bot.mvc.controller.util;

import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import com.tratsiak.telegram.bot.mvc.model.Token;

public class TokenExtractor {
    private final static String JWT_NAME = "jwt";

    public static String access(Session session) {
        Token token = (Token) session.getEntity(JWT_NAME);
        return "Bearer " + token.getAccess();
    }
}
