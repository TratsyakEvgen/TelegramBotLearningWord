package com.tratsiak.telegram.bot.mvc;

import com.tratsiak.telegram.bot.mvc.lib.core.BotMVC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class Config {

    private final BotMVC botMVC;

    @Autowired
    public Config(BotMVC botMVC) {
        this.botMVC = botMVC;
    }


    @Bean
    public BotSession getBot() throws TelegramApiException {
        return new TelegramBotsApi(DefaultBotSession.class).registerBot(botMVC);
    }

}
