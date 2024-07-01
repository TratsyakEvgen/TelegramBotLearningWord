package com.tratsiak.telegram.bot.learning.word.controller.util;

import com.tratsiak.telegram.bot.learning.word.model.Token;
import com.tratsiak.telegrambotmvc.core.session.Session;

public class TokenExtractor {
    private final static String JWT_NAME = "jwt";

    public static String access(Session session) {
        Token token = (Token) session.getEntity(JWT_NAME);
        return "Bearer " + token.getAccess();
    }
}
