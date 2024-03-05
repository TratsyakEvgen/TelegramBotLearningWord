package com.tratsiak.telegram.bot.mvc.lib.core;

import com.tratsiak.telegram.bot.mvc.lib.controller.ControllerMapper;
import com.tratsiak.telegram.bot.mvc.lib.controller.ControllerMapperException;
import com.tratsiak.telegram.bot.mvc.lib.session.BotSessionException;
import com.tratsiak.telegram.bot.mvc.lib.session.BotSessions;
import com.tratsiak.telegram.bot.mvc.lib.session.Session;
import com.tratsiak.telegram.bot.mvc.lib.view.BotView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

@Component
public class BotMVC extends TelegramLongPollingBot {


    private final ControllerMapper controllerMapper;
    private final BotSessions botSessions;

    private final String botName;

    @Autowired
    public BotMVC(@Value("${botToken}") String botToken, @Value("${botName}") String botName, ControllerMapper controllerMapper, BotSessions botSessions) {
        super(botToken);
        this.controllerMapper = controllerMapper;
        this.botName = botName;
        this.botSessions = botSessions;
    }

    @Override
    public void onUpdateReceived(Update update) {


        Session session;
        try {
            session = botSessions.getOrElseCreate(update);
        } catch (BotSessionException e) {
            e.printStackTrace();
            return;
        }

        try {
            BotView view = controllerMapper.executeControllerMethod(session.getCurrentCommand());

            for (PartialBotApiMethod<?> sendingMessage : view.getSendingMessages()) {

                Class<?> classMessage = sendingMessage.getClass();

                Arrays.stream(this.getClass().getMethods())
                        .filter(method -> method.getName().equals("execute"))
                        .filter(method -> Arrays.stream(method.getParameterTypes())
                                .allMatch(p -> p.equals(classMessage) | p.isAssignableFrom(classMessage)))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Method execute(" + classMessage + ") not found"))
                        .invoke(this, sendingMessage);

            }


        } catch (IllegalAccessException | InvocationTargetException | ControllerMapperException e) {
            e.printStackTrace();
            try {
                execute(SendMessage.builder().chatId(session.getId()).text("Произошла ошибка!").build());
            } catch (TelegramApiException ex) {
                e.printStackTrace();
            }

        }
    }

        @Override
        public String getBotUsername () {
            return botName;
        }
    }
