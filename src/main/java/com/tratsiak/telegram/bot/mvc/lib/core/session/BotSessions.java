package com.tratsiak.telegram.bot.mvc.lib.core.session;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

@Component
@EqualsAndHashCode
@ToString
public class BotSessions {

    private final Map<Long, Session> sessions = new HashMap<>();
    private final SessionInitializer sessionInitializer;
    private final SessionModifier sessionModifier;

    @Autowired
    public BotSessions(SessionInitializer sessionInitializer, SessionModifier sessionModifier) {
        this.sessionInitializer = sessionInitializer;
        this.sessionModifier = sessionModifier;
    }


    public Session getSession(Update update) throws SessionException {

        Session session;
        long userId;

        if (update.hasCallbackQuery()) {

            CallbackQuery query = update.getCallbackQuery();
            userId = query.getFrom().getId();

            session = getAndModify(userId);

            if (session == null){
                return null;
            }

            session.setCurrentCommand(query.getData());
            session.setNextCommand(null);
           // session.setText(query.getMessage().getText());

        } else {
            Message message;

            message = update.getMessage();
            userId = message.getFrom().getId();

            session = getAndModify(userId);

            if (session == null){
                return null;
            }

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


    private Session getAndModify(long id) throws SessionException {
        Session session = sessions.get(id);

        if (session == null) {
            try {
                session = sessionInitializer.init(id);
            } catch (Exception e) {
                throw new SessionException("Can't init session", e);
            }
        }

        if (session != null){
            try {
                sessionModifier.modify(session);
            } catch (Exception e) {
                throw new SessionException("Can't modify session", e);
            }
        }

        return session;
    }
}
