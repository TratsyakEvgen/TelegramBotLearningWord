package com.tratsiak.telegram.bot.learning.word.repository.impl;

import com.tratsiak.telegram.bot.learning.word.model.TrainingTranslateEngToRus;
import com.tratsiak.telegram.bot.learning.word.model.bean.ErrorResponse;
import com.tratsiak.telegram.bot.learning.word.repository.TrainingTranslateEngToRusRepository;
import com.tratsiak.telegram.bot.learning.word.repository.exception.LevelException;
import com.tratsiak.telegram.bot.learning.word.repository.exception.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class TrainingTranslateEngToRusRepositoryImpl
        extends WebClientRepository implements TrainingTranslateEngToRusRepository {

    @Autowired
    public TrainingTranslateEngToRusRepositoryImpl(WebClient webClient) {
        super(webClient);
    }

    @Override
    public TrainingTranslateEngToRus get(boolean isLearned, String access) throws RepositoryException {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/trainingsEngToRus")
                            .queryParam("isLearned", isLearned)
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .header(AUTH, access)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, resp -> resp.bodyToMono(ErrorResponse.class)
                            .flatMap(error -> Mono.error(
                                    new RepositoryException(LevelException.INFO, error.getMessage(), error.toString())
                            ))
                    )
                    .bodyToMono(TrainingTranslateEngToRus.class)
                    .block();
        } catch (RepositoryException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new RepositoryException(LevelException.ERROR, "Can't get training translate english to russian",
                    String.format("WebClient exception! isLearned %b, access %s", isLearned, access), e);
        }

    }

    @Override
    public long check(long learningWordId, long answer, String access) throws RepositoryException {
        try {
            String body = String.format("{\"learningWordId\": %d, \"answer\": %d }", learningWordId, answer);
            String result = webClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/trainingsEngToRus").build())
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
                    .bodyToMono(String.class)
                    .block();
            assert result != null;
            return Long.parseLong(result);
        } catch (RepositoryException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new RepositoryException(LevelException.ERROR,
                    "Can't check answer for training translate english to russian",
                    String.format("WebClient exception! learningWordId %d, answer %d, access %s",
                            learningWordId, answer, access),
                    e);
        }
    }
}
