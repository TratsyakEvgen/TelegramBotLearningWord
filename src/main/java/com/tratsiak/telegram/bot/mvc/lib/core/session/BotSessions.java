package com.tratsiak.telegram.bot.mvc.lib.core.session;

import lombok.*;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

@Component
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class BotSessions {
    private Map<Long, Session> sessions = new HashMap<>();

    public Session getOrElseCreate(Update update) {
        Session session;

        long userId;

        if (update.hasCallbackQuery()) {

            CallbackQuery query = update.getCallbackQuery();
            userId = query.getFrom().getId();

            session = getOrElseCreate(userId);

            session.setCurrentCommand(query.getData());
            session.setNextCommand(null);
            session.setText(query.getMessage().getText());


        } else {
            Message message;

            message = update.getMessage();
            userId = message.getFrom().getId();

            session = getOrElseCreate(userId);

            session.setText(message.getText());

            session.setCurrentCommand(session.getNextCommand());
            session.setNextCommand(null);
            if (message.isCommand()) {
                session.setCurrentCommand("/start");
            }

        }

        return session;
    }


    private Session getOrElseCreate(long id) {

        Session session = sessions.get(id);
        if (session == null) {
            session = new Session(id);
        }
        sessions.put(session.getId(), session);

        return session;
    }
}
