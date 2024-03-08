package com.tratsiak.telegram.bot.mvc.view;

import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.view.components.ComponentSendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ViewLearningWord {

    private final ComponentSendMessage componentSendMessage;

    @Autowired
    public ViewLearningWord(ComponentSendMessage componentSendMessage) {
        this.componentSendMessage = componentSendMessage;
    }

    public BotView delete(long id) {
        return new BotView(componentSendMessage.get(id, "Delete"));
    }


}
