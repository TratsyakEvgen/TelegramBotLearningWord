package com.tratsiak.telegram.bot.mvc.lib.components;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;


public interface ComponentInlineKeyboardMarkup {

    InlineKeyboardMarkup get(InlineKeyboardButton inlineKeyboardButton);

    InlineKeyboardMarkup get(List<InlineKeyboardButton> inlineKeyboardButtons);

    InlineKeyboardMarkup.InlineKeyboardMarkupBuilder row(
            InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder,
            InlineKeyboardButton inlineKeyboardButton);

    InlineKeyboardMarkup.InlineKeyboardMarkupBuilder row(
            InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder,
            List<InlineKeyboardButton> inlineKeyboardButtons);

}
