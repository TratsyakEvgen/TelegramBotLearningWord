package com.tratsiak.telegram.bot.learning.word.repository;

import com.tratsiak.telegram.bot.learning.word.model.Page;
import com.tratsiak.telegram.bot.learning.word.model.Word;
import com.tratsiak.telegram.bot.learning.word.repository.exception.RepositoryException;

public interface WordRepository {
    Word get(long id, String access) throws RepositoryException;

    Page<Word> getWords(String part, int page, String access) throws RepositoryException;
}
