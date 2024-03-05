package com.tratsiak.telegram.bot.mvc.view;

import com.tratsiak.telegram.bot.mvc.lib.view.BotView;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


public class View {
    public static BotView start() {

        SendMessage hello = SendMessage.builder()
                .chatId(781198053L)
                .text("Hello")
                .build();
        SendMessage myName = SendMessage.builder()
                .chatId(781198053L)
                .text("I am bot")
                .build();

        BotView botView = new BotView();
        botView.put(hello);
        botView.put(myName);
        return botView;
    }
}
