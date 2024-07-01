package com.tratsiak.telegram.bot.learning.word.repository;

import com.tratsiak.telegram.bot.learning.word.model.TrainingTranslateEngToRus;
import com.tratsiak.telegram.bot.learning.word.repository.exception.RepositoryException;

public interface TrainingTranslateEngToRusRepository {
    TrainingTranslateEngToRus get(boolean isLearned, String access) throws RepositoryException;

    long check(long learningWordId, long answer, String access) throws RepositoryException;

}
