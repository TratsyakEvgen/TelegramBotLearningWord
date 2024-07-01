package com.tratsiak.telegram.bot.learning.word.controller;

import com.tratsiak.telegram.bot.learning.word.controller.exception.ExceptionHandler;
import com.tratsiak.telegram.bot.learning.word.controller.util.TokenExtractor;
import com.tratsiak.telegram.bot.learning.word.model.TrainingTranslateEngToRus;
import com.tratsiak.telegram.bot.learning.word.model.Word;
import com.tratsiak.telegram.bot.learning.word.repository.LearningWordRepository;
import com.tratsiak.telegram.bot.learning.word.repository.TrainingTranslateEngToRusRepository;
import com.tratsiak.telegram.bot.learning.word.repository.WordRepository;
import com.tratsiak.telegram.bot.learning.word.repository.exception.RepositoryException;
import com.tratsiak.telegram.bot.learning.word.view.TrainingTranslate;
import com.tratsiak.telegrambotmvc.annotation.BotController;
import com.tratsiak.telegrambotmvc.annotation.BotRequestMapping;
import com.tratsiak.telegrambotmvc.core.session.Session;
import com.tratsiak.telegrambotmvc.core.view.View;
import org.springframework.beans.factory.annotation.Autowired;

@BotController
@BotRequestMapping(path = "/engToRus")
public class TrainingEngToRusController {

    private final TrainingTranslateEngToRusRepository repository;
    private final LearningWordRepository learningWordRepository;
    private final WordRepository wordRepository;
    private final TrainingTranslate trainingTranslate;
    private final ExceptionHandler handler;

    @Autowired
    public TrainingEngToRusController(TrainingTranslateEngToRusRepository repository, LearningWordRepository learningWordRepository, WordRepository wordRepository, TrainingTranslate trainingTranslate, ExceptionHandler handler) {
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
            TrainingTranslateEngToRus engToRus = repository.get(isLearned, TokenExtractor.access(session));
            return trainingTranslate.training(session.getId(), engToRus, isLearned);
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
            TrainingTranslateEngToRus engToRus = repository.get(!status, TokenExtractor.access(session));
            return trainingTranslate.training(session.getId(), engToRus, !status);
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
                TrainingTranslateEngToRus engToRus = repository.get(status, TokenExtractor.access(session));
                return trainingTranslate.training(userId, engToRus, status);
            }

            Word word = wordRepository.get(answer, TokenExtractor.access(session));
            return trainingTranslate.mistake(userId, word, "/engToRus/get?isLearned=" + status);
        } catch (RepositoryException e) {
            throw handler.handle(e);
        }

    }
}
