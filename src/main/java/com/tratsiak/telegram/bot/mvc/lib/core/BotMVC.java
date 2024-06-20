package com.tratsiak.telegram.bot.mvc.lib.core;

import com.tratsiak.telegram.bot.mvc.lib.core.dispatcher.DispatcherRequests;
import com.tratsiak.telegram.bot.mvc.lib.core.dispatcher.ExecuteMethodDispatcherRequestsException;
import com.tratsiak.telegram.bot.mvc.lib.core.dispatcher.DispatchersRequestsInitializer;
import com.tratsiak.telegram.bot.mvc.lib.core.exeption.handler.ErrorViewer;
import com.tratsiak.telegram.bot.mvc.lib.core.exeption.handler.MapperExceptionHandler;
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
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collection;
import java.util.List;

@Component
@Slf4j
public class BotMVC extends TelegramLongPollingBot {

    private final DispatchersRequestsInitializer dispatchersRequestsInitializer;
    private final PathValidator pathValidator;
    private final PathParser pathParser;
    private final MethodExecutor methodExecutor;
    private final BotSession botSession;
    private final MapperExceptionHandler mapperExceptionHandler;
    private final ErrorViewer errorViewer;
    private final String botName;

    @Autowired
    public BotMVC(@Value("${botToken}") String botToken,
                  DispatchersRequestsInitializer dispatchersRequestsInitializer,
                  PathValidator pathValidator,
                  PathParser pathParser,
                  MethodExecutor methodExecutor,
                  @Value("${botName}") String botName,
                  DefaultBotSession botSession, MapperExceptionHandler mapperExceptionHandler, ErrorViewer errorViewer) {
        super(botToken);
        this.dispatchersRequestsInitializer = dispatchersRequestsInitializer;
        this.pathValidator = pathValidator;
        this.pathParser = pathParser;
        this.methodExecutor = methodExecutor;
        this.botName = botName;
        this.botSession = botSession;
        this.mapperExceptionHandler = mapperExceptionHandler;
        this.errorViewer = errorViewer;
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

        Session session = botSession.getSession(update);


        BotView botView = null;

        try {

            if (update.hasCallbackQuery()) {
                execute(new AnswerCallbackQuery(update.getCallbackQuery().getId()));
            }

            String path = session.getCurrentCommand();

            pathValidator.isValidPathWithParams(path);
            session.setCurrentCommand(pathParser.getPath(path));
            session.setParameters(pathParser.getParam(path));


            Collection<DispatcherRequests> mappers = dispatchersRequestsInitializer.getDispatcherRequestsMap().values();
            for (DispatcherRequests mapper : mappers) {
                botView = mapper.executeMethod(path, session);
                if (botView != null) {
                    break;
                }
            }


        } catch (Exception e) {
            botView = mapperExceptionHandler.handle(e, session);
            if (botView == null) {
                log.error("Core error", e);
            }
        }

        if (botView == null) {
            botView = errorViewer.getDefaultError(session);
        }

        List<PartialBotApiMethod<?>> messages = botView.getSendingMessages();
        if (messages == null) {
            log.error("Messages should not be null");
            botView = errorViewer.getDefaultError(session);
        }

        try {
            for (PartialBotApiMethod<?> sendingMessage : botView.getSendingMessages()) {
                methodExecutor.executeVoidMethodWithParameter(this, sendingMessage, "execute");
            }
        } catch (MethodExecutorException e) {
            log.error("Core error", e);
        }

        session.clearTemporary();
    }
}
