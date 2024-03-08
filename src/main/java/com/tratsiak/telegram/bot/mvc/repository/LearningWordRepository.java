package com.tratsiak.telegram.bot.mvc.repository;

import com.tratsiak.telegram.bot.mvc.model.LearningWord;

public interface LearningWordRepository {
    LearningWord create(long wordId) throws RepositoryException;

    LearningWord update(long id, boolean status) throws RepositoryException;

    void delete(long id) throws RepositoryException;
}
