package com.tratsiak.telegram.bot.mvc.view;

import com.tratsiak.telegram.bot.mvc.lib.annotation.BotStaticResource;
import com.tratsiak.telegram.bot.mvc.lib.annotation.BotViewStaticResource;
import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import com.tratsiak.telegram.bot.mvc.view.components.ComponentInlineKeyboardButton;
import com.tratsiak.telegram.bot.mvc.view.components.ComponentInlineKeyboardMarkup;
import com.tratsiak.telegram.bot.mvc.view.components.ComponentSendMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@BotViewStaticResource
public class StaticResource {
    private final ComponentSendMessage componentSendMessage;
    private final ComponentInlineKeyboardButton componentInlineKeyboardButton;
    private final ComponentInlineKeyboardMarkup componentInlineKeyboardMarkup;

    @Autowired
    public StaticResource(ComponentSendMessage componentSendMessage, ComponentInlineKeyboardButton componentInlineKeyboardButton, ComponentInlineKeyboardMarkup componentInlineKeyboardMarkup) {
        this.componentSendMessage = componentSendMessage;
        this.componentInlineKeyboardButton = componentInlineKeyboardButton;
        this.componentInlineKeyboardMarkup = componentInlineKeyboardMarkup;
    }

    @BotStaticResource(path = "/start")
    public BotView start(Session session) {
        return new BotView(componentSendMessage.get(session.getId(), "Hello!",
                componentInlineKeyboardMarkup.get(List.of(
                        componentInlineKeyboardButton.get("Find word", "/static/findWord"),
                        componentInlineKeyboardButton.get("My dictionary", "/"),
                        componentInlineKeyboardButton.get("Training", "/")
                ))));
    }

    @BotStaticResource(path = "/static/findWord")
    private BotView findWord(Session session) {
        session.setNextCommand("/words/find");
        return new BotView(componentSendMessage.get(session.getId(), "Enter word or part of it:"));
    }
}
