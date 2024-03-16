package com.tratsiak.telegram.bot.mvc.repository;

import com.tratsiak.telegram.bot.mvc.model.AuthTelegramApp;
import com.tratsiak.telegram.bot.mvc.model.Token;

public interface TokenRepository {
    Token getNewToken(AuthTelegramApp auth) throws RepositoryException;

    Token updateToken(Token token) throws RepositoryException;
}
