package com.tratsiak.telegram.bot.mvc.repository.impl;

import com.tratsiak.telegram.bot.mvc.model.LearningWord;
import com.tratsiak.telegram.bot.mvc.model.Page;
import com.tratsiak.telegram.bot.mvc.model.bean.ErrorResponse;
import com.tratsiak.telegram.bot.mvc.repository.LearningWordRepository;
import com.tratsiak.telegram.bot.mvc.repository.exception.LevelException;
import com.tratsiak.telegram.bot.mvc.repository.exception.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class LearningWordRepositoryImpl extends WebClientRepository implements LearningWordRepository {

    @Autowired
    public LearningWordRepositoryImpl(WebClient webClient) {
        super(webClient);
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
                    .onStatus(HttpStatusCode::isError, resp -> resp.bodyToMono(ErrorResponse.class)
                            .flatMap(error -> Mono.error(
                                    new RepositoryException(LevelException.INFO, error.getMessage(), error.toString())
                            ))
                    )
                    .bodyToMono(new ParameterizedTypeReference<Page<LearningWord>>() {
                    })
                    .block();
        } catch (RepositoryException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new RepositoryException(LevelException.ERROR, "Can't get dictionary",
                    String.format("WebClient exception! page %d, access %s", page, access), e);
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
                    .onStatus(HttpStatusCode::isError, resp -> resp.bodyToMono(ErrorResponse.class)
                            .flatMap(error -> Mono.error(
                                    new RepositoryException(LevelException.INFO, error.getMessage(), error.toString())
                            ))
                    )
                    .bodyToMono(LearningWord.class)
                    .block();
        } catch (RepositoryException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new RepositoryException(LevelException.ERROR, "Can't add to dictionary",
                    String.format("WebClient exception! page %d, access %s", wordId, access), e);
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
                    .onStatus(HttpStatusCode::isError, resp -> resp.bodyToMono(ErrorResponse.class)
                            .flatMap(error -> Mono.error(
                                    new RepositoryException(LevelException.INFO, error.getMessage(), error.toString())
                            ))
                    )
                    .bodyToMono(LearningWord.class)
                    .block();
        } catch (RepositoryException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new RepositoryException(LevelException.ERROR, "Can't update",
                    String.format("WebClient exception! id %d, status %b access %s", id, status, access), e);
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
                    .onStatus(HttpStatusCode::isError, resp -> resp.bodyToMono(ErrorResponse.class)
                            .flatMap(error -> Mono.error(
                                    new RepositoryException(LevelException.INFO, error.getMessage(), error.toString())
                            ))
                    )
                    .bodyToMono(LearningWord.class)
                    .block();
        } catch (RepositoryException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new RepositoryException(LevelException.ERROR, "Can't delete",
                    String.format("WebClient exception! id %d, access %s", id, access), e);
        }
    }
}
