package com.tratsiak.telegram.bot.mvc.lib.components;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public interface ComponentInlineKeyboardButton {
    InlineKeyboardButton get(String text, String callbackData);
}
