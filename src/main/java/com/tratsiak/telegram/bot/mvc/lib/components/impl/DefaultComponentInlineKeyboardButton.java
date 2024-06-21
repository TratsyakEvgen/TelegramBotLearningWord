package com.tratsiak.telegram.bot.mvc.lib.components.impl;

import com.tratsiak.telegram.bot.mvc.lib.components.ComponentInlineKeyboardButton;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class DefaultComponentInlineKeyboardButton implements ComponentInlineKeyboardButton {
    @Override
    public InlineKeyboardButton get(String text, String callbackData) {
        return InlineKeyboardButton.builder().text(text).callbackData(callbackData).build();
    }
}
