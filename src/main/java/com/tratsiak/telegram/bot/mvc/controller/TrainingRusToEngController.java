package com.tratsiak.telegram.bot.mvc.controller;

import com.tratsiak.telegram.bot.mvc.controller.exception.ExceptionHandler;
import com.tratsiak.telegram.bot.mvc.controller.util.TokenExtractor;
import com.tratsiak.telegram.bot.mvc.lib.annotation.BotController;
import com.tratsiak.telegram.bot.mvc.lib.annotation.BotRequestMapping;
import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import com.tratsiak.telegram.bot.mvc.model.TrainingTranslateRusToEng;
import com.tratsiak.telegram.bot.mvc.model.Word;
import com.tratsiak.telegram.bot.mvc.repository.LearningWordRepository;
import com.tratsiak.telegram.bot.mvc.repository.TrainingTranslateRusToEngRepository;
import com.tratsiak.telegram.bot.mvc.repository.WordRepository;
import com.tratsiak.telegram.bot.mvc.repository.exception.RepositoryException;
import com.tratsiak.telegram.bot.mvc.view.TrainingTranslate;

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
    public BotView get(Session session) {
        boolean isLearned = Boolean.parseBoolean(session.getParam("isLearned"));
        try {
            TrainingTranslateRusToEng rusToEng = repository.get(isLearned, TokenExtractor.access(session));
            return trainingTranslate.training(session.getId(), rusToEng, isLearned);
        } catch (RepositoryException e) {
            throw handler.handle(e);
        }

    }


    @BotRequestMapping(path = "/update")
    public BotView update(Session session) {
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
    public BotView check(Session session) {

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
            return trainingTranslate.mistake(userId, word, "/rusToEng/get?status=" + status);
        } catch (RepositoryException e) {
            throw handler.handle(e);
        }

    }
}
