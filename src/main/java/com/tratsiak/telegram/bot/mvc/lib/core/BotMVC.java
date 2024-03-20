package com.tratsiak.telegram.bot.mvc.lib.core;

import com.tratsiak.telegram.bot.mvc.lib.core.mapper.Mapper;
import com.tratsiak.telegram.bot.mvc.lib.core.mapper.MapperException;
import com.tratsiak.telegram.bot.mvc.lib.core.mapper.impl.ControllerMapper;
import com.tratsiak.telegram.bot.mvc.lib.core.session.BotSessions;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import com.tratsiak.telegram.bot.mvc.lib.core.session.SessionException;
import com.tratsiak.telegram.bot.mvc.lib.util.BotPath;
import com.tratsiak.telegram.bot.mvc.lib.util.NotValidPathException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

@Component
public class BotMVC extends TelegramLongPollingBot {

    private final Mapper controllerMapper;
    private final Mapper viewMapper;
    private final BotSessions botSessions;

    private final String botName;

    @Autowired
    public BotMVC(@Value("${botToken}") String botToken,
                  @Value("${botName}") String botName,
                  ControllerMapper controllerMapper,
                  Mapper viewMapper,
                  BotSessions botSessions) {
        super(botToken);
        this.controllerMapper = controllerMapper;
        this.botName = botName;
        this.viewMapper = viewMapper;
        this.botSessions = botSessions;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Session session;
        try {
            if (update.hasCallbackQuery()) {
                execute(new AnswerCallbackQuery(update.getCallbackQuery().getId()));
            }
            session = botSessions.getSession(update);

            if (session == null) {
                return;
            }
        } catch (TelegramApiException | SessionException e) {
            e.printStackTrace();
            return;
        }


        String path = session.getCurrentCommand();

        try {
            BotPath botPath = BotPath.parse(path);
            path = botPath.getPath();

            session.setCurrentCommand(botPath.getPath());
            session.setParameters(botPath.getParameters());

            BotView botView = controllerMapper.executeMethod(path, session);

            if (botView == null) {
                botView = viewMapper.executeMethod(path, session);
            }

            if (botView == null) {
                sendExceptionMessage(session.getId());
                return;
            }

            for (PartialBotApiMethod<?> sendingMessage : botView.getSendingMessages()) {

                Class<?> classMessage = sendingMessage.getClass();
                Arrays.stream(TelegramLongPollingBot.class.getMethods())
                        .filter(method -> method.getName().equals("execute"))
                        .filter(method -> Arrays.stream(method.getParameterTypes())
                                .allMatch(p -> p.equals(classMessage) | p.isAssignableFrom(classMessage)))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Method execute(" + classMessage + ") not found"))
                        .invoke(this, sendingMessage);

            }


        } catch (MapperException | NotValidPathException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            sendExceptionMessage(session.getId());
        }

        session.clearTemporary();

    }


    @Override
    public String getBotUsername() {
        return botName;
    }


    private void sendExceptionMessage(long id) {
        try {
            execute(SendMessage.builder().chatId(id).text("Error!!!").build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
