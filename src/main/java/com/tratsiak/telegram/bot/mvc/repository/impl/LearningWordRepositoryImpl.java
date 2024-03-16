package com.tratsiak.telegram.bot.mvc.repository.impl;

import com.tratsiak.telegram.bot.mvc.model.LearningWord;
import com.tratsiak.telegram.bot.mvc.model.Page;
import com.tratsiak.telegram.bot.mvc.model.Word;
import com.tratsiak.telegram.bot.mvc.repository.LearningWordRepository;
import com.tratsiak.telegram.bot.mvc.repository.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class LearningWordRepositoryImpl implements LearningWordRepository {
    private static final String AUTH = "Authorization";

    private final WebClient webClient;

    @Autowired
    public LearningWordRepositoryImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Page<LearningWord> get(int page, String access) throws RepositoryException {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/learningWords")
                            .queryParam("page", page)
                            .queryParam("size", "5")
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .header(AUTH, access)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, error -> Mono.error(
                            new RuntimeException(String.valueOf(error.statusCode()))))
                    .bodyToMono(new ParameterizedTypeReference<Page<LearningWord>>() {
                    })
                    .block();
        } catch (RuntimeException e) {
            throw new RepositoryException("Can't get learning words by page: " + page, e);
        }
    }

    @Override
    public LearningWord create(long wordId, String access) throws RepositoryException {
        try {
            String body = String.format("{\"id\": %d}", wordId);
            return webClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/learningWords").build())
                    .accept(MediaType.APPLICATION_JSON)
                    .header(AUTH, access)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, error -> Mono.error(
                            new RuntimeException(String.valueOf(error.statusCode()))))
                    .bodyToMono(LearningWord.class)
                    .block();
        } catch (RuntimeException e) {
            throw new RepositoryException("Can't create learning word by id word " + wordId, e);
        }

    }

    @Override
    public LearningWord update(long id, boolean status, String access) throws RepositoryException {
        try {
            String body = String.format("{\"id\": %d, \"learnedStatus\": %b}", id, status);
            return webClient.put()
                    .uri(uriBuilder -> uriBuilder.path("/learningWords").build())
                    .accept(MediaType.APPLICATION_JSON)
                    .header(AUTH, access)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, error -> Mono.error(
                            new RuntimeException(String.valueOf(error.statusCode()))))
                    .bodyToMono(LearningWord.class)
                    .block();
        } catch (RuntimeException e) {
            throw new RepositoryException(String.format("Can't update learning word %d status %b", id, status), e);
        }
    }

    @Override
    public void delete(long id, String access) throws RepositoryException {
        try {
            webClient.delete()
                    .uri(uriBuilder -> uriBuilder.path("/learningWords/" + id).build())
                    .accept(MediaType.APPLICATION_JSON)
                    .header(AUTH, access)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, error -> Mono.error(
                            new RuntimeException(String.valueOf(error.statusCode()))))
                    .bodyToMono(LearningWord.class)
                    .block();
        } catch (RuntimeException e) {
            throw new RepositoryException("Can't delete learning word " + id, e);
        }
    }
}
