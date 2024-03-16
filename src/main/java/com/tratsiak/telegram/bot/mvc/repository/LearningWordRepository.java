package com.tratsiak.telegram.bot.mvc.repository;

import com.tratsiak.telegram.bot.mvc.model.LearningWord;
import com.tratsiak.telegram.bot.mvc.model.Page;

public interface LearningWordRepository {

    Page<LearningWord> get(int page, String access) throws RepositoryException;

    LearningWord create(long wordId, String access) throws RepositoryException;

    LearningWord update(long id, boolean status, String access) throws RepositoryException;

    void delete(long id, String access) throws RepositoryException;
}
