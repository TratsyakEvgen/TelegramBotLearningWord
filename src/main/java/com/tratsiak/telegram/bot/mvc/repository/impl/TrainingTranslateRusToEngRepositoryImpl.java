package com.tratsiak.telegram.bot.mvc.repository.impl;

import com.tratsiak.telegram.bot.mvc.model.TrainingTranslateRusToEng;
import com.tratsiak.telegram.bot.mvc.model.bean.ErrorResponse;
import com.tratsiak.telegram.bot.mvc.repository.TrainingTranslateRusToEngRepository;
import com.tratsiak.telegram.bot.mvc.repository.exception.LevelException;
import com.tratsiak.telegram.bot.mvc.repository.exception.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class TrainingTranslateRusToEngRepositoryImpl implements TrainingTranslateRusToEngRepository {

    private static final String AUTH = "Authorization";
    private final WebClient webClient;

    @Autowired
    public TrainingTranslateRusToEngRepositoryImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public TrainingTranslateRusToEng get(boolean isLearned, String access) throws RepositoryException {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/trainingsRusToEng")
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
                    .bodyToMono(TrainingTranslateRusToEng.class)
                    .block();
        } catch (RepositoryException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new RepositoryException(LevelException.ERROR, "Can't get training translate russian to english",
                    String.format("WebClient exception! isLearned %b, access %s", isLearned, access), e);
        }
    }

    @Override
    public long check(long learningWordId, long answer, String access) throws RepositoryException {
        try {
            String body = String.format("{\"learningWordId\": %d, \"answer\": %d }", learningWordId, answer);
            String result = webClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/trainingsRusToEng").build())
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
                    "Can't get training translate russian to english",
                    String.format("WebClient exception! learningWordId %d, answer %d, access %s",
                            learningWordId, answer, access),
                    e);
        }
    }
}
