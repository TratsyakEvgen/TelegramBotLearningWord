package com.tratsiak.telegram.bot.mvc.repository.impl;

import com.tratsiak.telegram.bot.mvc.model.Page;
import com.tratsiak.telegram.bot.mvc.model.Word;
import com.tratsiak.telegram.bot.mvc.repository.RepositoryException;
import com.tratsiak.telegram.bot.mvc.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class WordRepositoryImpl implements WordRepository {

    private static final String AUTH = "Authorization";
    private final WebClient webClient;

    @Autowired
    public WordRepositoryImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Word get(long id, String access) throws RepositoryException {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/words/" + id)
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .header(AUTH, access)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, error -> Mono.error(
                            new RuntimeException(String.valueOf(error.statusCode()))))
                    .bodyToMono(Word.class)
                    .block();
        } catch (RuntimeException e) {
            throw new RepositoryException("Can't get word", e);
        }
    }

    @Override
    public List<Word> getWords(String part, String access) throws RepositoryException {
        try {
            Page<Word> page = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/words")
                            .queryParam("part", part)
                            .queryParam("page", "0")
                            .queryParam("size", "5")
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .header(AUTH, access)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, error -> Mono.error(
                            new RuntimeException(String.valueOf(error.statusCode()))))
                    .bodyToMono(new ParameterizedTypeReference<Page<Word>>() {
                    })
                    .block();
            assert page != null;
            return page.getContent();
        } catch (RuntimeException e) {
            throw new RepositoryException("Can't get words by part: " + part, e);
        }
    }


}
