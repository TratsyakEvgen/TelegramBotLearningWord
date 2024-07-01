package com.tratsiak.telegram.bot.learning.word.controller;

import com.tratsiak.telegram.bot.learning.word.controller.exception.ExceptionHandler;
import com.tratsiak.telegram.bot.learning.word.controller.util.TokenExtractor;
import com.tratsiak.telegram.bot.learning.word.model.Page;
import com.tratsiak.telegram.bot.learning.word.model.Word;
import com.tratsiak.telegram.bot.learning.word.repository.AudioRepository;
import com.tratsiak.telegram.bot.learning.word.repository.WordRepository;
import com.tratsiak.telegram.bot.learning.word.repository.exception.RepositoryException;
import com.tratsiak.telegram.bot.learning.word.view.ViewWord;
import com.tratsiak.telegrambotmvc.annotation.BotController;
import com.tratsiak.telegrambotmvc.annotation.BotRequestMapping;
import com.tratsiak.telegrambotmvc.core.session.Session;
import com.tratsiak.telegrambotmvc.core.view.View;
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
    private View getWords(Session session) {
        int page = Integer.parseInt(session.getParam("page"));
        String part = session.getTextMessage();
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
    private View getWord(Session session) {
        try {
            Word word = wordRepository.get(Long.parseLong(session.getParam("id")), TokenExtractor.access(session));
            return viewWord.word(session.getId(), word);
        } catch (RepositoryException e) {
            throw handler.handle(e);
        }
    }

    @BotRequestMapping(path = "/getAudio")
    private View getAudio(Session session) {
        try {
            File file = audioRepository.getAudioFile(session.getParam("file"));
            return viewWord.sound(session.getId(), file);
        } catch (RepositoryException e) {
            throw handler.handle(e);
        }
    }


}
