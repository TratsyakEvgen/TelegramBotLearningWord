package com.tratsiak.telegram.bot.mvc.lib.session;

import com.tratsiak.telegram.bot.mvc.lib.util.BotPath;
import com.tratsiak.telegram.bot.mvc.lib.util.NotValidPathException;
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

    public Session getOrElseCreate(Update update) throws BotSessionException {
        Session session;

        long userId;

        if (update.hasCallbackQuery()) {

            CallbackQuery query = update.getCallbackQuery();
            userId = query.getFrom().getId();

            session = getOrElseCreate(userId);
            session.setText(query.getMessage().getText());
            String path = query.getData();

            try {
                if (path != null){
                    BotPath botPath = BotPath.parse(query.getData());
                    session.setCurrentCommand(botPath.getCommand());
                    session.setParameters(botPath.getParameters());
                }
            } catch (NotValidPathException e) {
                throw new BotSessionException("Can't parse path " + path, e);
            }



        } else {
            Message message;

            message = update.getMessage();
            userId = message.getFrom().getId();

            session = getOrElseCreate(userId);

            String text = message.getText();
            if (message.isCommand()) {
                session.setCurrentCommand("/start");
            }
            session.setText(text);
        }

        return session;
    }

    private Session getOrElseCreate(long id) {
        if (sessions.containsKey(id)) {
            return sessions.get(id);
        }
        return new Session(id);
    }
}
