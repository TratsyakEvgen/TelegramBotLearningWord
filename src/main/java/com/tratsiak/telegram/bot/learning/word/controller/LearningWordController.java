package com.tratsiak.telegram.bot.learning.word.controller;

import com.tratsiak.telegram.bot.learning.word.controller.exception.ExceptionHandler;
import com.tratsiak.telegram.bot.learning.word.controller.util.TokenExtractor;
import com.tratsiak.telegram.bot.learning.word.model.LearningWord;
import com.tratsiak.telegram.bot.learning.word.model.Page;
import com.tratsiak.telegram.bot.learning.word.model.Word;
import com.tratsiak.telegram.bot.learning.word.repository.LearningWordRepository;
import com.tratsiak.telegram.bot.learning.word.repository.WordRepository;
import com.tratsiak.telegram.bot.learning.word.repository.exception.RepositoryException;
import com.tratsiak.telegram.bot.learning.word.view.ViewLearningWord;
import com.tratsiak.telegram.bot.learning.word.view.ViewWord;
import com.tratsiak.telegrambotmvc.annotation.BotController;
import com.tratsiak.telegrambotmvc.annotation.BotRequestMapping;
import com.tratsiak.telegrambotmvc.core.session.Session;
import com.tratsiak.telegrambotmvc.core.view.View;
import org.springframework.beans.factory.annotation.Autowired;

@BotController
@BotRequestMapping(path = "/learningWords")
public class LearningWordController {
    private final ViewWord viewWord;
    private final ViewLearningWord viewLearningWord;
    private final WordRepository wordRepository;
    private final LearningWordRepository learningWordRepository;
    private final ExceptionHandler handler;

    @Autowired
    public LearningWordController(ViewWord viewWord,
                                  ViewLearningWord viewLearningWord,
                                  WordRepository wordRepository,
                                  LearningWordRepository learningWordRepository, ExceptionHandler handler) {
        this.viewWord = viewWord;
        this.viewLearningWord = viewLearningWord;
        this.wordRepository = wordRepository;
        this.learningWordRepository = learningWordRepository;
        this.handler = handler;
    }

    @BotRequestMapping(path = "/add")
    private View add(Session session) {
        try {
            long id = Long.parseLong(session.getParam("id"));

            LearningWord learningWord = learningWordRepository.create(id, TokenExtractor.access(session));
            Word word = wordRepository.get(learningWord.getWord().getId(), TokenExtractor.access(session));
            return viewWord.word(session.getId(), word);
        } catch (RepositoryException e) {
            throw handler.handle(e);
        }

    }

    @BotRequestMapping(path = "/delete")
    private View delete(Session session) {
        try {
            learningWordRepository.delete(Long.parseLong(session.getParam("id")), TokenExtractor.access(session));

            return viewLearningWord.delete(session.getId());
        } catch (RepositoryException e) {
            throw handler.handle(e);
        }
    }

    @BotRequestMapping(path = "/update")
    private View update(Session session) {
        try {
            long id = Long.parseLong(session.getParam("id"));
            boolean status = Boolean.parseBoolean(session.getParam("status"));

            LearningWord learningWord = learningWordRepository.update(id, status, TokenExtractor.access(session));
            Word word = wordRepository.get(learningWord.getWord().getId(), TokenExtractor.access(session));

            return viewWord.word(session.getId(), word);
        } catch (RepositoryException e) {
            throw handler.handle(e);
        }
    }

    @BotRequestMapping(path = "/statistic")
    private View getStatistic(Session session) {
        try {
            long id = Long.parseLong(session.getParam("id"));
            Word word = wordRepository.get(id, TokenExtractor.access(session));
            return viewLearningWord.statistic(session.getId(), word);
        } catch (RepositoryException e) {
            throw handler.handle(e);
        }
    }

    @BotRequestMapping(path = "/dictionary")
    private View getDictionary(Session session) {
        try {
            int page = Integer.parseInt(session.getParam("page"));
            Page<LearningWord> wordPage = learningWordRepository.get(page, TokenExtractor.access(session));

            return viewLearningWord.dictionary(session.getId(), wordPage);
        } catch (RepositoryException e) {
            throw handler.handle(e);
        }
    }


}
