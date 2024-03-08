package com.tratsiak.telegram.bot.mvc.repository.impl;

import com.tratsiak.telegram.bot.mvc.model.LearningWord;
import com.tratsiak.telegram.bot.mvc.repository.LearningWordRepository;
import com.tratsiak.telegram.bot.mvc.repository.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class LearningWordRepositoryImpl implements LearningWordRepository {

    private final WebClient webClient;

    @Autowired
    public LearningWordRepositoryImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public LearningWord create(long wordId) throws RepositoryException {
        try {
            String body = String.format("{\"id\": %d}", wordId);
            return webClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/learningWords").build())
                    .accept(MediaType.APPLICATION_JSON)
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
    public LearningWord update(long id, boolean status) throws RepositoryException {
        try {
            String body = String.format("{\"id\": %d, \"learnedStatus\": %b}", id, status);
            return webClient.put()
                    .uri(uriBuilder -> uriBuilder.path("/learningWords").build())
                    .accept(MediaType.APPLICATION_JSON)
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
    public void delete(long id) throws RepositoryException {
        try {
            webClient.delete()
                    .uri(uriBuilder -> uriBuilder.path("/learningWords/" + id).build())
                    .accept(MediaType.APPLICATION_JSON)
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
