package com.tratsiak.telegram.bot.mvc.controller;

import com.tratsiak.telegram.bot.mvc.lib.annotation.BotController;
import com.tratsiak.telegram.bot.mvc.lib.annotation.BotRequestMapping;
import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import com.tratsiak.telegram.bot.mvc.model.LearningWord;
import com.tratsiak.telegram.bot.mvc.model.Word;
import com.tratsiak.telegram.bot.mvc.repository.LearningWordRepository;
import com.tratsiak.telegram.bot.mvc.repository.RepositoryException;
import com.tratsiak.telegram.bot.mvc.repository.WordRepository;
import com.tratsiak.telegram.bot.mvc.view.ViewLearningWord;
import com.tratsiak.telegram.bot.mvc.view.ViewWord;
import org.springframework.beans.factory.annotation.Autowired;

@BotController
@BotRequestMapping(path = "/learningWords")
public class LearningWordController {
    private final ViewWord viewWord;
    private final ViewLearningWord viewLearningWord;
    private final WordRepository wordRepository;
    private final LearningWordRepository learningWordRepository;

    @Autowired
    public LearningWordController(ViewWord viewWord,
                                  ViewLearningWord viewLearningWord,
                                  WordRepository wordRepository,
                                  LearningWordRepository learningWordRepository) {
        this.viewWord = viewWord;
        this.viewLearningWord = viewLearningWord;
        this.wordRepository = wordRepository;
        this.learningWordRepository = learningWordRepository;
    }

    @BotRequestMapping(path = "/add")
    private BotView add(Session session) throws RepositoryException {

        long id = Long.parseLong(session.getParam("id"));
        LearningWord learningWord = learningWordRepository.create(id);
        Word word = wordRepository.get(learningWord.getWord().getId());
        return viewWord.word(session.getId(), word);
    }

    @BotRequestMapping(path = "/delete")
    private BotView delete(Session session) throws RepositoryException {

        learningWordRepository.delete(Long.parseLong(session.getParam("id")));
        return viewLearningWord.delete(session.getId());
    }

    @BotRequestMapping(path = "/update")
    private BotView update(Session session) throws RepositoryException {

        long id = Long.parseLong(session.getParam("id"));
        boolean status = Boolean.parseBoolean(session.getParam("status"));
        LearningWord learningWord = learningWordRepository.update(id, status);
        Word word = wordRepository.get(learningWord.getWord().getId());
        return viewWord.word(session.getId(), word);
    }




}
