package com.tratsiak.telegram.bot.mvc.lib.components.impl;

import com.tratsiak.telegram.bot.mvc.lib.components.ComponentSendMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Component
public class DefaultComponentSendMessage implements ComponentSendMessage {
    @Override
    public SendMessage get(long id, String text) {
        return SendMessage.builder().chatId(id).text(text).build();
    }

    @Override
    public SendMessage get(long id, String text, ReplyKeyboard replyKeyboard) {
        return SendMessage.builder().chatId(id).text(text).replyMarkup(replyKeyboard).build();
    }

}
