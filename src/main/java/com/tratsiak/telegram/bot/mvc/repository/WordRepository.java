package com.tratsiak.telegram.bot.mvc.repository;

import com.tratsiak.telegram.bot.mvc.model.Page;
import com.tratsiak.telegram.bot.mvc.model.Word;
import com.tratsiak.telegram.bot.mvc.repository.exception.RepositoryException;

import java.util.List;

public interface WordRepository {
    Word get(long id, String access) throws RepositoryException;

    Page<Word> getWords(String part, int page, String access) throws RepositoryException;
}
