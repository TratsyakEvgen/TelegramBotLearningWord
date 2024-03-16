package com.tratsiak.telegram.bot.mvc.repository;

import com.tratsiak.telegram.bot.mvc.model.Word;

import java.util.List;

public interface WordRepository {
    Word get(long id, String access) throws RepositoryException;

    List<Word> getWords(String part, String access) throws RepositoryException;
}
