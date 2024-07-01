package com.tratsiak.telegram.bot.learning.word.repository;

import com.tratsiak.telegram.bot.learning.word.model.Token;
import com.tratsiak.telegram.bot.learning.word.model.bean.AuthTelegramApp;
import com.tratsiak.telegram.bot.learning.word.repository.exception.RepositoryException;

public interface TokenRepository {
    Token getNewToken(AuthTelegramApp auth) throws RepositoryException;

    Token updateToken(Token token) throws RepositoryException;
}
