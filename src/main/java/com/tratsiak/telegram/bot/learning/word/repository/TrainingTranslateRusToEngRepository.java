package com.tratsiak.telegram.bot.learning.word.repository;

import com.tratsiak.telegram.bot.learning.word.model.TrainingTranslateRusToEng;
import com.tratsiak.telegram.bot.learning.word.repository.exception.RepositoryException;

public interface TrainingTranslateRusToEngRepository {
    TrainingTranslateRusToEng get(boolean isLearned, String access) throws RepositoryException;

    long check(long learningWordId, long answer, String access) throws RepositoryException;
}
