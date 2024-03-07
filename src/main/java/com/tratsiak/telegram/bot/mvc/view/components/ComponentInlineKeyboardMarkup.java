package com.tratsiak.telegram.bot.mvc.view.components;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class ComponentInlineKeyboardMarkup {

    public InlineKeyboardMarkup get(InlineKeyboardButton inlineKeyboardButton) {
        return get(List.of(inlineKeyboardButton));
    }

    public InlineKeyboardMarkup get(List<InlineKeyboardButton> inlineKeyboardButtons) {
        return InlineKeyboardMarkup.builder().keyboardRow(inlineKeyboardButtons).build();
    }

    public InlineKeyboardMarkup.InlineKeyboardMarkupBuilder row(
            InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder,
            InlineKeyboardButton inlineKeyboardButton) {
        return row(builder, List.of(inlineKeyboardButton));

    }

    public InlineKeyboardMarkup.InlineKeyboardMarkupBuilder row(
            InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder,
            List<InlineKeyboardButton> inlineKeyboardButtons) {
        return builder.keyboardRow(inlineKeyboardButtons);


    }

}
