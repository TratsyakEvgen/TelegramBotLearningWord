package com.tratsiak.telegram.bot.mvc.repository.impl;

import com.tratsiak.telegram.bot.mvc.model.Page;
import com.tratsiak.telegram.bot.mvc.model.Word;
import com.tratsiak.telegram.bot.mvc.model.bean.ErrorResponse;
import com.tratsiak.telegram.bot.mvc.repository.WordRepository;
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
public class WordRepositoryImpl extends WebClientRepository implements WordRepository {

    @Autowired
    public WordRepositoryImpl(WebClient webClient) {
        super(webClient);
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
                    .onStatus(HttpStatusCode::isError, resp -> resp.bodyToMono(ErrorResponse.class)
                            .flatMap(error -> Mono.error(
                                    new RepositoryException(LevelException.INFO, error.getMessage(), error.toString())
                            ))
                    )
                    .bodyToMono(Word.class)
                    .block();
        } catch (RepositoryException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new RepositoryException(LevelException.ERROR, "Can't get word",
                    String.format("WebClient exception! id %d, access %s", id, access), e);
        }
    }

    @Override
    public Page<Word> getWords(String part, int page, String access) throws RepositoryException {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/words")
                            .queryParam("part", part)
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
                    .bodyToMono(new ParameterizedTypeReference<Page<Word>>() {
                    })
                    .block();
        } catch (RuntimeException e) {
            throw new RepositoryException(LevelException.ERROR, "Can't get words",
                    String.format("WebClient exception! part %s, access %s", part, access), e);
        }
    }


}
