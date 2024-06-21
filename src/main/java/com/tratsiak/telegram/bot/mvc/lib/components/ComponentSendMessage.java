package com.tratsiak.telegram.bot.mvc.lib.components;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;


public interface ComponentSendMessage {
    SendMessage get(long id, String text);

    SendMessage get(long id, String text, ReplyKeyboard replyKeyboard);

}
