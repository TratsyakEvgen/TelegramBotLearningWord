package com.tratsiak.telegram.bot.mvc.lib.core;

import com.tratsiak.telegram.bot.mvc.lib.core.mapper.MethodMapper;
import com.tratsiak.telegram.bot.mvc.lib.core.mapper.MethodMapperException;
import com.tratsiak.telegram.bot.mvc.lib.core.mapper.MethodMapperInitializer;
import com.tratsiak.telegram.bot.mvc.lib.core.path.NotValidPathException;
import com.tratsiak.telegram.bot.mvc.lib.core.path.PathParser;
import com.tratsiak.telegram.bot.mvc.lib.core.path.PathValidator;
import com.tratsiak.telegram.bot.mvc.lib.core.session.BotSession;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import com.tratsiak.telegram.bot.mvc.lib.core.session.SessionException;
import com.tratsiak.telegram.bot.mvc.lib.core.session.impl.DefaultBotSession;
import com.tratsiak.telegram.bot.mvc.lib.util.reflection.method.executor.MethodExecutor;
import com.tratsiak.telegram.bot.mvc.lib.util.reflection.method.executor.MethodExecutorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collection;

@Component
@Slf4j
public class BotMVC extends TelegramLongPollingBot {

    private final MethodMapperInitializer methodMapperInitializer;
    private final PathValidator pathValidator;
    private final PathParser pathParser;
    private final MethodExecutor methodExecutor;
    private final BotSession botSession;
    private final String botName;

    @Autowired
    public BotMVC(@Value("${botToken}") String botToken,
                  MethodMapperInitializer methodMapperInitializer, PathValidator pathValidator, PathParser pathParser, MethodExecutor methodExecutor,
                  @Value("${botName}") String botName,
                  DefaultBotSession botSession) {
        super(botToken);
        this.methodMapperInitializer = methodMapperInitializer;
        this.pathValidator = pathValidator;
        this.pathParser = pathParser;
        this.methodExecutor = methodExecutor;
        this.botName = botName;
        this.botSession = botSession;
    }

    @Override
    public void onUpdateReceived(Update update) {
        new Thread(() -> sendAnswer(update)).start();
    }


    @Override
    public String getBotUsername() {
        return botName;
    }


    private void sendAnswer(Update update) {

        try {
            if (update.hasCallbackQuery()) {
                execute(new AnswerCallbackQuery(update.getCallbackQuery().getId()));
            }
        } catch (TelegramApiException e) {
            log.error("Error answer callback query", e);
            return;
        }


        Session session;
        try {
            session = botSession.getSession(update);
        } catch (SessionException e) {
            log.error("Session error", e);
            return;
        }

        if (session == null) {
            return;
        }


        String path = session.getCurrentCommand();

        try {
            pathValidator.isValidPathWithParams(path);
            session.setCurrentCommand(pathParser.getPath(path));
            session.setParameters(pathParser.getParam(path));

            BotView botView = null;
            Collection<MethodMapper> mappers = methodMapperInitializer.getMethodMappers().values();
            for (MethodMapper mapper : mappers) {
                botView = mapper.executeMethod(path, session);
                if (botView != null) {
                    break;
                }
            }

            if (botView == null) {
                log.error(String.format("Endpoint '%s' not found", path));
                sendExceptionMessage(session.getId());
                return;
            }

            for (PartialBotApiMethod<?> sendingMessage : botView.getSendingMessages()) {
                methodExecutor.executeVoidMethodWithParameter(this, sendingMessage, "execute");
            }


        } catch (MethodMapperException | NotValidPathException | MethodExecutorException e) {
            log.error("Error execute method", e);
            sendExceptionMessage(session.getId());
        }

        session.clearTemporary();
    }


    private void sendExceptionMessage(long id) {
        try {
            execute(SendMessage.builder().chatId(id).text("Error!!!").build());
        } catch (TelegramApiException e) {
            log.error("Error send exception message", e);
        }
    }
}
