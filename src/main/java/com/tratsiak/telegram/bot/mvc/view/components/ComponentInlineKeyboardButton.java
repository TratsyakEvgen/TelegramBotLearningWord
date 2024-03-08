package com.tratsiak.telegram.bot.mvc.view.components;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class ComponentInlineKeyboardButton {

    public InlineKeyboardButton get(String text, String callbackData) {
        return InlineKeyboardButton.builder().text(text).callbackData(callbackData).build();
    }
}
