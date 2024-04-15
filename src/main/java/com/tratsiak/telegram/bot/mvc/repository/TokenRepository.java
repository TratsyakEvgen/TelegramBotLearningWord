package com.tratsiak.telegram.bot.mvc.repository;

import com.tratsiak.telegram.bot.mvc.model.Token;
import com.tratsiak.telegram.bot.mvc.model.bean.AuthTelegramApp;
import com.tratsiak.telegram.bot.mvc.repository.exception.RepositoryException;

public interface TokenRepository {
    Token getNewToken(AuthTelegramApp auth) throws RepositoryException;

    Token updateToken(Token token) throws RepositoryException;
}
