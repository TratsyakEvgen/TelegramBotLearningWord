package com.tratsiak.telegram.bot.mvc.controller;

import com.tratsiak.telegram.bot.mvc.controller.exception.ExceptionHandler;
import com.tratsiak.telegram.bot.mvc.controller.util.TokenExtractor;
import com.tratsiak.telegram.bot.mvc.lib.annotation.BotController;
import com.tratsiak.telegram.bot.mvc.lib.annotation.BotRequestMapping;
import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import com.tratsiak.telegram.bot.mvc.model.Page;
import com.tratsiak.telegram.bot.mvc.model.Word;
import com.tratsiak.telegram.bot.mvc.repository.AudioRepository;
import com.tratsiak.telegram.bot.mvc.repository.WordRepository;
import com.tratsiak.telegram.bot.mvc.repository.exception.RepositoryException;
import com.tratsiak.telegram.bot.mvc.view.ViewWord;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

@BotController
@BotRequestMapping(path = "/words")
public class WordController {

    private final ViewWord viewWord;
    private final WordRepository wordRepository;
    private final AudioRepository audioRepository;
    private final ExceptionHandler handler;

    @Autowired
    public WordController(ViewWord viewWord,
                          WordRepository wordRepository,
                          AudioRepository audioRepository,
                          ExceptionHandler handler) {
        this.viewWord = viewWord;
        this.wordRepository = wordRepository;
        this.audioRepository = audioRepository;
        this.handler = handler;
    }

    @BotRequestMapping(path = "/find")
    private BotView getWords(Session session) {
        int page = Integer.parseInt(session.getParam("page"));
        String part = session.getText();
        if (part == null) {
            part = session.getParam("part");
        }
        try {
            Page<Word> words = wordRepository.getWords(part, page, TokenExtractor.access(session));
            return viewWord.findWords(session.getId(), part, words);
        } catch (RepositoryException e) {
            throw handler.handle(e);
        }
    }


    @BotRequestMapping(path = "/get")
    private BotView getWord(Session session) {
        try {
            Word word = wordRepository.get(Long.parseLong(session.getParam("id")), TokenExtractor.access(session));
            return viewWord.word(session.getId(), word);
        } catch (RepositoryException e) {
            throw handler.handle(e);
        }
    }

    @BotRequestMapping(path = "/getAudio")
    private BotView getAudio(Session session) {
        try {
            File file = audioRepository.getAudioFile(session.getParam("file"));
            return viewWord.sound(session.getId(), file);
        } catch (RepositoryException e) {
            throw handler.handle(e);
        }
    }


}
