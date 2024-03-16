package com.tratsiak.telegram.bot.mvc.repository.impl;

import com.tratsiak.telegram.bot.mvc.model.AuthTelegramApp;
import com.tratsiak.telegram.bot.mvc.model.Token;
import com.tratsiak.telegram.bot.mvc.repository.RepositoryException;
import com.tratsiak.telegram.bot.mvc.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class TokenRepositoryImpl implements TokenRepository {
    private final WebClient webClient;

    @Autowired
    public TokenRepositoryImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Token getNewToken(AuthTelegramApp auth) throws RepositoryException {
        try {
            return webClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/tokens").build())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(auth)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, error -> Mono.error(
                            new RuntimeException(String.valueOf(error.statusCode()))))
                    .bodyToMono(Token.class)
                    .block();
        } catch (RuntimeException e) {
            throw new RepositoryException("Can't get new token", e);
        }
    }

    @Override
    public Token updateToken(Token token) throws RepositoryException {
        try {
            return webClient.put()
                    .uri(uriBuilder -> uriBuilder.path("/tokens").build())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(token)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, error -> Mono.error(
                            new RuntimeException(String.valueOf(error.statusCode()))))
                    .bodyToMono(Token.class)
                    .block();
        } catch (RuntimeException e) {
            throw new RepositoryException("Can't update token", e);
        }
    }


}
