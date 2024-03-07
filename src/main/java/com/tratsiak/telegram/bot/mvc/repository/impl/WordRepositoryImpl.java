package com.tratsiak.telegram.bot.mvc.repository.impl;

import com.tratsiak.telegram.bot.mvc.model.Word;
import com.tratsiak.telegram.bot.mvc.repository.RepositoryException;
import com.tratsiak.telegram.bot.mvc.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class WordRepositoryImpl implements WordRepository {
    private final WebClient webClient;

    @Autowired
    public WordRepositoryImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Word get(long id) throws RepositoryException {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/words/" + id)
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, error -> Mono.error(
                            new RuntimeException(String.valueOf(error.statusCode()))))
                    .bodyToMono(Word.class)
                    .block();
        } catch (RuntimeException e) {
            throw new RepositoryException("Can't get word", e);
        }
    }
}
