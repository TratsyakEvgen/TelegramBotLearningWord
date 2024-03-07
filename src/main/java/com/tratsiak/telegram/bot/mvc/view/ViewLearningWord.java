package com.tratsiak.telegram.bot.mvc.view;

import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.model.LearningWord;
import com.tratsiak.telegram.bot.mvc.model.Word;
import com.tratsiak.telegram.bot.mvc.view.components.ComponentInlineKeyboardButton;
import com.tratsiak.telegram.bot.mvc.view.components.ComponentInlineKeyboardMarkup;
import com.tratsiak.telegram.bot.mvc.view.components.ComponentSendAudio;
import com.tratsiak.telegram.bot.mvc.view.components.ComponentSendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;

@Component
public class ViewLearningWord {

    private final ComponentSendMessage componentSendMessage;
    private final ComponentInlineKeyboardButton componentInlineKeyboardButton;
    private final ComponentInlineKeyboardMarkup componentInlineKeyboardMarkup;
    private final ComponentSendAudio componentSendAudio;

    private final String audio;

    @Autowired
    public ViewLearningWord(ComponentSendMessage componentSendMessage,
                            ComponentInlineKeyboardButton componentInlineKeyboardButton,
                            ComponentInlineKeyboardMarkup componentInlineKeyboardMarkup,
                            ComponentSendAudio componentSendAudio,
                            @Value("${resource}") String audio) {
        this.componentSendMessage = componentSendMessage;
        this.componentInlineKeyboardButton = componentInlineKeyboardButton;
        this.componentInlineKeyboardMarkup = componentInlineKeyboardMarkup;
        this.componentSendAudio = componentSendAudio;
        this.audio = audio;
    }


}
