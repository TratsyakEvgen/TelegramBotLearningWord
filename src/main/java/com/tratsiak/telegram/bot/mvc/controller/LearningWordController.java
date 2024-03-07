package com.tratsiak.telegram.bot.mvc.controller;

import com.tratsiak.telegram.bot.mvc.lib.annotation.BotController;
import com.tratsiak.telegram.bot.mvc.lib.annotation.BotRequestMapping;
import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import com.tratsiak.telegram.bot.mvc.model.LearningWord;
import com.tratsiak.telegram.bot.mvc.model.Word;
import com.tratsiak.telegram.bot.mvc.repository.RepositoryException;
import com.tratsiak.telegram.bot.mvc.repository.WordRepository;
import com.tratsiak.telegram.bot.mvc.view.ViewLearningWord;
import com.tratsiak.telegram.bot.mvc.view.ViewWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@BotController
@BotRequestMapping(path = "/learningWords")
public class LearningWordController {

    private final WebClient webClient;

    private final ViewWord viewWord;
    private final ViewLearningWord viewLearningWord;

    private final WordRepository wordRepository;

    @Autowired
    public LearningWordController(WebClient webClient, ViewWord viewWord, ViewLearningWord viewLearningWord, WordRepository wordRepository) {
        this.webClient = webClient;
        this.viewWord = viewWord;
        this.viewLearningWord = viewLearningWord;
        this.wordRepository = wordRepository;
    }

    @BotRequestMapping(path = "/add")
    private BotView addLearningWord(Session session) throws RepositoryException {
        String body = String.format("{\"id\": %d}", Long.valueOf(session.getParam("id")));
        LearningWord learningWord = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/learningWords").build())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, error -> Mono.error(
                        new RuntimeException(String.valueOf(error.statusCode()))))
                .bodyToMono(LearningWord.class)
                .block();

        assert learningWord != null;
        Word word = wordRepository.get(learningWord.getWord().getId());
        return viewWord.word(session.getId(), word);
    }


}
