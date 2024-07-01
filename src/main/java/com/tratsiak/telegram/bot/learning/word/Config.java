package com.tratsiak.telegram.bot.learning.word;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class Config {
    @Bean
    public WebClient webClient(@Value("${resource}") String resource) {
        return WebClient.builder().baseUrl(resource).build();
    }

}
