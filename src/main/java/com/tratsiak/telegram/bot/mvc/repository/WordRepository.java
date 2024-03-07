package com.tratsiak.telegram.bot.mvc.repository;

import com.tratsiak.telegram.bot.mvc.model.Word;

public interface WordRepository {
    Word get(long id) throws RepositoryException;
}
