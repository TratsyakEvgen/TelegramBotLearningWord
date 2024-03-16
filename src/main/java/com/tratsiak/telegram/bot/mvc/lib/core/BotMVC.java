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
import java.util.List;

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

        if (update.hasCallbackQuery()) {
            try {
                execute(new AnswerCallbackQuery(update.getCallbackQuery().getId()));
            } catch (TelegramApiException e) {
                e.printStackTrace();
                return;
            }
        }

        Session session = null;
        try {
            session = botSessions.getSession(update);
        } catch (SessionException e) {
            e.printStackTrace();
            return;
        }

        if (session == null){
            return;
        }

        String command = session.getCurrentCommand();


        if (command == null) {
            session.clearTemporary();
            return;
        }

        try {
            BotView botView = executeCommand(command, session);
            send(botView.getSendingMessages());

        } catch (MapperException e) {
            Throwable cause = findCause(e);
            if (cause instanceof ResponseException) {
                sendExceptionMessage(session.getId(), cause.getMessage());
            } else {
                e.printStackTrace();
                sendExceptionMessage(session.getId(), "Error!!!");
            }
        } catch (IllegalAccessException | InvocationTargetException | NotValidPathException e) {
            e.printStackTrace();
            sendExceptionMessage(session.getId(), "Error!!!");
        } finally {
            session.clearTemporary();
        }
    }


    @Override
    public String getBotUsername() {
        return botName;
    }


    private BotView executeCommand(String path, Session session) throws NotValidPathException, MapperException {
        BotPath botPath = BotPath.parse(path);
        path = botPath.getPath();
        session.setCurrentCommand(botPath.getPath());
        session.setParameters(botPath.getParameters());

        BotView botView = controllerMapper.executeMethod(path, session);

        if (botView == null) {
            botView = viewMapper.executeMethod(path, session);
        }

        if (botView == null) {
            throw new MapperException("Path " + path + " not found");
        }


        return botView;
    }

    private void send(List<PartialBotApiMethod<?>> partialBotApiMethods)
            throws InvocationTargetException, IllegalAccessException {
        for (PartialBotApiMethod<?> sendingMessage : partialBotApiMethods) {

            Class<?> classMessage = sendingMessage.getClass();
            Arrays.stream(TelegramLongPollingBot.class.getMethods())
                    .filter(method -> method.getName().equals("execute"))
                    .filter(method -> Arrays.stream(method.getParameterTypes())
                            .allMatch(p -> p.equals(classMessage) | p.isAssignableFrom(classMessage)))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Method execute(" + classMessage + ") not found"))
                    .invoke(this, sendingMessage);

        }
    }


    private void sendExceptionMessage(long id, String text) {
        try {
            execute(SendMessage.builder().chatId(id).text(text).build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private Throwable findCause(Throwable throwable) {
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }
}
