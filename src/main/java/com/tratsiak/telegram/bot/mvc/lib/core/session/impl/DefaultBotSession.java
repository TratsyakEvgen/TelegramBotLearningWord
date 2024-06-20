package com.tratsiak.telegram.bot.mvc.lib.core.session.impl;

import com.tratsiak.telegram.bot.mvc.lib.core.session.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component

@EqualsAndHashCode
@ToString
public class DefaultBotSession implements BotSession {

    private final Map<Long, Session> sessions;
    private final SessionInitializer sessionInitializer;
    private final SessionModifier sessionModifier;

    @Autowired
    public DefaultBotSession(SessionInitializer sessionInitializer, SessionModifier sessionModifier) {
        this.sessionInitializer = sessionInitializer;
        this.sessionModifier = sessionModifier;
        this.sessions = new ConcurrentHashMap<>();
    }

    @Override
    public Session getSession(Update update) {

        Session session;
        long userId;

        if (update.hasCallbackQuery()) {

            CallbackQuery query = update.getCallbackQuery();
            userId = query.getFrom().getId();

            session = getOrCreateAndModifySession(userId);

            session.setCurrentCommand(query.getData());
            session.setNextCommand(null);

        } else {
            Message message;

            message = update.getMessage();
            userId = message.getFrom().getId();

            session = getOrCreateAndModifySession(userId);

            session.setText(message.getText());
            session.setCurrentCommand(session.getNextCommand());
            session.setNextCommand(null);

            if (message.isCommand()) {
                session.setCurrentCommand("/start");
            }

        }

        sessions.put(session.getId(), session);
        return session;
    }


    private Session getOrCreateAndModifySession(long id) {
        Optional<Session> optionalSession = Optional.ofNullable(sessions.get(id));
        optionalSession.or(() -> sessionInitializer.init(id)).ifPresent(sessionModifier::modify);
        return optionalSession.orElseThrow(() -> new SessionException("Session should not be null"));
    }
}
