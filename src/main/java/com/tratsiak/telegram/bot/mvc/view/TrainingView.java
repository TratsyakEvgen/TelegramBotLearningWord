package com.tratsiak.telegram.bot.mvc.view;

import com.tratsiak.telegram.bot.mvc.lib.components.ComponentInlineKeyboardButton;
import com.tratsiak.telegram.bot.mvc.lib.components.ComponentInlineKeyboardMarkup;
import com.tratsiak.telegram.bot.mvc.lib.components.ComponentSendMessage;
import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.model.TrainingTranslateEngToRus;
import org.springframework.stereotype.Component;

@Component
public class TrainingView {

    private final ComponentSendMessage compSendMsg;
    private final ComponentInlineKeyboardButton compInlineBtn;
    private final ComponentInlineKeyboardMarkup compInlineMarkup;

    public TrainingView(ComponentSendMessage compSendMsg,
                        ComponentInlineKeyboardButton compInlineBtn,
                        ComponentInlineKeyboardMarkup compInlineMarkup) {
        this.compSendMsg = compSendMsg;
        this.compInlineBtn = compInlineBtn;
        this.compInlineMarkup = compInlineMarkup;
    }

    public BotView training(TrainingTranslateEngToRus engToRus){
        return null;

    }
}
