package com.tratsiak.telegram.bot.mvc.controller;

import com.tratsiak.telegram.bot.mvc.lib.annotation.BotController;
import com.tratsiak.telegram.bot.mvc.lib.annotation.BotRequestMapping;
import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.lib.core.ResponseException;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import com.tratsiak.telegram.bot.mvc.model.Page;
import com.tratsiak.telegram.bot.mvc.model.Word;
import com.tratsiak.telegram.bot.mvc.repository.RepositoryException;
import com.tratsiak.telegram.bot.mvc.repository.WordRepository;
import com.tratsiak.telegram.bot.mvc.view.ViewWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@BotController
@BotRequestMapping(path = "/words")
public class WordController {

    private final WebClient webClient;
    private final ViewWord viewWord;

    private final WordRepository wordRepository;

    @Autowired
    public WordController(WebClient webClient, ViewWord viewWord, WordRepository wordRepository) {
        this.webClient = webClient;
        this.viewWord = viewWord;
        this.wordRepository = wordRepository;
    }

    @BotRequestMapping(path = "/find")
    private BotView getWords(Session session) throws ResponseException {
        Page<Word> page = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/words")
                        .queryParam("part", session.getText())
                        .queryParam("page", "0")
                        .queryParam("size", "5")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, error -> Mono.error(
                        new RuntimeException(String.valueOf(error.statusCode()))))
                .bodyToMono(new ParameterizedTypeReference<Page<Word>>() {
                })
                .block();


        assert page != null;
        if (page.getContent().isEmpty()) {
            throw new ResponseException("Not found!");
        }
        return viewWord.findWords(session.getId(), page.getContent());
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
        return viewWord.sound(session.getId(), word);
    }




}
