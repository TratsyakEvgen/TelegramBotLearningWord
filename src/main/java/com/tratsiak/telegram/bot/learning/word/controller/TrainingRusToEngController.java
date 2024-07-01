package com.tratsiak.telegram.bot.learning.word.controller;

import com.tratsiak.telegram.bot.learning.word.controller.exception.ExceptionHandler;
import com.tratsiak.telegram.bot.learning.word.controller.util.TokenExtractor;
import com.tratsiak.telegram.bot.learning.word.model.TrainingTranslateRusToEng;
import com.tratsiak.telegram.bot.learning.word.model.Word;
import com.tratsiak.telegram.bot.learning.word.repository.LearningWordRepository;
import com.tratsiak.telegram.bot.learning.word.repository.TrainingTranslateRusToEngRepository;
import com.tratsiak.telegram.bot.learning.word.repository.WordRepository;
import com.tratsiak.telegram.bot.learning.word.repository.exception.RepositoryException;
import com.tratsiak.telegram.bot.learning.word.view.TrainingTranslate;
import com.tratsiak.telegrambotmvc.annotation.BotController;
import com.tratsiak.telegrambotmvc.annotation.BotRequestMapping;
import com.tratsiak.telegrambotmvc.core.session.Session;
import com.tratsiak.telegrambotmvc.core.view.View;

@BotController
@BotRequestMapping(path = "/rusToEng")
public class TrainingRusToEngController {

    private final TrainingTranslateRusToEngRepository repository;
    private final LearningWordRepository learningWordRepository;
    private final WordRepository wordRepository;
    private final TrainingTranslate trainingTranslate;
    private final ExceptionHandler handler;

    public TrainingRusToEngController(TrainingTranslateRusToEngRepository repository, LearningWordRepository learningWordRepository, WordRepository wordRepository, TrainingTranslate trainingTranslate, ExceptionHandler handler) {
        this.repository = repository;
        this.learningWordRepository = learningWordRepository;
        this.wordRepository = wordRepository;
        this.trainingTranslate = trainingTranslate;
        this.handler = handler;
    }


    @BotRequestMapping(path = "/get")
    public View get(Session session) {
        boolean isLearned = Boolean.parseBoolean(session.getParam("isLearned"));
        try {
            TrainingTranslateRusToEng rusToEng = repository.get(isLearned, TokenExtractor.access(session));
            return trainingTranslate.training(session.getId(), rusToEng, isLearned);
        } catch (RepositoryException e) {
            throw handler.handle(e);
        }

    }


    @BotRequestMapping(path = "/update")
    public View update(Session session) {
        long id = Long.parseLong(session.getParam("id"));
        boolean status = Boolean.parseBoolean(session.getParam("status"));
        try {
            learningWordRepository.update(id, status, TokenExtractor.access(session));
            TrainingTranslateRusToEng rusToEng = repository.get(!status, TokenExtractor.access(session));
            return trainingTranslate.training(session.getId(), rusToEng, !status);
        } catch (RepositoryException e) {
            throw handler.handle(e);
        }

    }

    @BotRequestMapping(path = "/check")
    public View check(Session session) {

        long learningWordId = Long.parseLong(session.getParam("id"));
        long wordId = Long.parseLong(session.getParam("ans"));
        boolean status = Boolean.parseBoolean(session.getParam("status"));
        long userId = session.getId();

        try {
            long answer = repository.check(learningWordId, wordId, TokenExtractor.access(session));

            if (answer == wordId) {
                TrainingTranslateRusToEng rusToEng = repository.get(status, TokenExtractor.access(session));
                return trainingTranslate.training(userId, rusToEng, status);
            }

            Word word = wordRepository.get(answer, TokenExtractor.access(session));
            return trainingTranslate.mistake(userId, word, "/rusToEng/get?isLearned=" + status);
        } catch (RepositoryException e) {
            throw handler.handle(e);
        }

    }
}
