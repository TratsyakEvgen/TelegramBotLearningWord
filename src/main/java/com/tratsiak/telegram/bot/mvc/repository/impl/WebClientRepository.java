package com.tratsiak.telegram.bot.mvc.repository.impl;

import org.springframework.web.reactive.function.client.WebClient;

public class WebClientRepository {
    protected final static String AUTH = "Authorization";
    protected final WebClient webClient;

    public WebClientRepository(WebClient webClient) {
        this.webClient = webClient;
    }
}
