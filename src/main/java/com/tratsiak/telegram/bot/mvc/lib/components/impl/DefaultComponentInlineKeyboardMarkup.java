package com.tratsiak.telegram.bot.mvc.lib.components.impl;

import com.tratsiak.telegram.bot.mvc.lib.components.ComponentInlineKeyboardMarkup;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class DefaultComponentInlineKeyboardMarkup implements ComponentInlineKeyboardMarkup {
    @Override
    public InlineKeyboardMarkup get(InlineKeyboardButton inlineKeyboardButton) {
        return get(List.of(inlineKeyboardButton));
    }

    @Override
    public InlineKeyboardMarkup get(List<InlineKeyboardButton> inlineKeyboardButtons) {
        return InlineKeyboardMarkup.builder().keyboardRow(inlineKeyboardButtons).build();
    }

    @Override
    public InlineKeyboardMarkup.InlineKeyboardMarkupBuilder row(
            InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder,
            InlineKeyboardButton inlineKeyboardButton) {
        return row(builder, List.of(inlineKeyboardButton));

    }

    @Override
    public InlineKeyboardMarkup.InlineKeyboardMarkupBuilder row(
            InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder,
            List<InlineKeyboardButton> inlineKeyboardButtons) {
        return builder.keyboardRow(inlineKeyboardButtons);


    }

}
