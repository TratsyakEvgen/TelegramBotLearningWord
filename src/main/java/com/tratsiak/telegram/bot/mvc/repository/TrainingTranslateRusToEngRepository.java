package com.tratsiak.telegram.bot.mvc.repository;

import com.tratsiak.telegram.bot.mvc.model.TrainingTranslateRusToEng;
import com.tratsiak.telegram.bot.mvc.repository.exception.RepositoryException;

public interface TrainingTranslateRusToEngRepository {
    TrainingTranslateRusToEng get(boolean isLearned, String access) throws RepositoryException;

    long check(long learningWordId, long answer, String access) throws RepositoryException;
}
