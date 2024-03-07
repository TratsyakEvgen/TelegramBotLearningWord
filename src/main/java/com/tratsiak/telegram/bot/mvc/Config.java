package com.tratsiak.telegram.bot.mvc;

import com.tratsiak.telegram.bot.mvc.lib.core.BotMVC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class Config {


    @Bean
    public BotSession getBot(BotMVC botMVC) throws TelegramApiException {
        return new TelegramBotsApi(DefaultBotSession.class).registerBot(botMVC);
    }

    @Bean
    public WebClient webClient(@Value("${resource}") String resource) {
        return WebClient.builder().baseUrl(resource).build();
    }

}
