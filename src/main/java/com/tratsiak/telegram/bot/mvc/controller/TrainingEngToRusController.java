package com.tratsiak.telegram.bot.mvc.controller;

import com.tratsiak.telegram.bot.mvc.controller.exception.ExceptionHandler;
import com.tratsiak.telegram.bot.mvc.controller.util.TokenExtractor;
import com.tratsiak.telegram.bot.mvc.lib.annotation.BotController;
import com.tratsiak.telegram.bot.mvc.lib.annotation.BotRequestMapping;
import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.lib.core.mapper.ResponseException;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import com.tratsiak.telegram.bot.mvc.model.TrainingTranslateEngToRus;
import com.tratsiak.telegram.bot.mvc.repository.TrainingTranslateEngToRusRepository;
import com.tratsiak.telegram.bot.mvc.repository.exception.RepositoryException;
import com.tratsiak.telegram.bot.mvc.view.TrainingView;
import org.springframework.beans.factory.annotation.Autowired;

@BotController
@BotRequestMapping(path = "/trainings/engToRus")
public class TrainingEngToRusController {

    private final TrainingTranslateEngToRusRepository repository;
    private final TrainingView trainingView;
    private final ExceptionHandler handler;

    @Autowired
    public TrainingEngToRusController(TrainingTranslateEngToRusRepository repository, TrainingView trainingView, ExceptionHandler handler) {
        this.repository = repository;
        this.trainingView = trainingView;
        this.handler = handler;
    }

    @BotRequestMapping(path = "/get")
    public BotView get(Session session) {
        boolean isLearned = Boolean.parseBoolean(session.getParam("isLearned"));
        try {
            TrainingTranslateEngToRus engToRus = repository.get(isLearned, TokenExtractor.access(session));
            return null;
        } catch (RepositoryException e) {
            throw handler.handle(e);
        }

    }
}
