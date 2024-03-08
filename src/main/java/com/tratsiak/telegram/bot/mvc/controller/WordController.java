package com.tratsiak.telegram.bot.mvc.controller;

import com.tratsiak.telegram.bot.mvc.lib.annotation.BotController;
import com.tratsiak.telegram.bot.mvc.lib.annotation.BotRequestMapping;
import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.lib.core.ResponseException;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import com.tratsiak.telegram.bot.mvc.model.Word;
import com.tratsiak.telegram.bot.mvc.repository.AudioRepository;
import com.tratsiak.telegram.bot.mvc.repository.RepositoryException;
import com.tratsiak.telegram.bot.mvc.repository.WordRepository;
import com.tratsiak.telegram.bot.mvc.view.ViewWord;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;

@BotController
@BotRequestMapping(path = "/words")
public class WordController {

    private final ViewWord viewWord;
    private final WordRepository wordRepository;

    private final AudioRepository audioRepository;

    @Autowired
    public WordController(ViewWord viewWord, WordRepository wordRepository, AudioRepository audioRepository) {
        this.viewWord = viewWord;
        this.wordRepository = wordRepository;
        this.audioRepository = audioRepository;
    }

    @BotRequestMapping(path = "/find")
    private BotView getWords(Session session) throws ResponseException, RepositoryException {
        List<Word> words = wordRepository.getWords(session.getText());
        if (words.isEmpty()) {
            throw new ResponseException("Not found!");
        }
        return viewWord.findWords(session.getId(), words);
    }


    @BotRequestMapping(path = "/get")
    private BotView getWord(Session session) throws RepositoryException {
        Word word = wordRepository.get(Long.parseLong(session.getParam("id")));
        assert word != null;
        return viewWord.word(session.getId(), word);
    }

    @BotRequestMapping(path = "/getAudio")
    private BotView getAudio(Session session) throws RepositoryException {
        Word word = wordRepository.get(Long.parseLong(session.getParam("id")));
        File file = audioRepository.getAudioFile(word.getEnglish());
        return viewWord.sound(session.getId(), file);
    }


}
