package com.tratsiak.telegram.bot.mvc.view;

import com.tratsiak.telegram.bot.mvc.lib.annotation.BotStaticResource;
import com.tratsiak.telegram.bot.mvc.lib.annotation.BotViewStaticResource;
import com.tratsiak.telegram.bot.mvc.lib.components.ComponentInlineKeyboardButton;
import com.tratsiak.telegram.bot.mvc.lib.components.ComponentInlineKeyboardMarkup;
import com.tratsiak.telegram.bot.mvc.lib.components.ComponentSendMessage;
import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

@BotViewStaticResource
public class StaticResource {
    private final ComponentSendMessage compSendMsg;
    private final ComponentInlineKeyboardButton compInlineBtn;
    private final ComponentInlineKeyboardMarkup compInlineMarkup;

    @Autowired
    public StaticResource(ComponentSendMessage compSendMsg,
                          ComponentInlineKeyboardButton compInlineBtn,
                          ComponentInlineKeyboardMarkup compInlineMarkup) {
        this.compSendMsg = compSendMsg;
        this.compInlineBtn = compInlineBtn;
        this.compInlineMarkup = compInlineMarkup;
    }

    @BotStaticResource(path = "/start")
    public BotView start(Session session) {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder = InlineKeyboardMarkup.builder();
        compInlineMarkup.row(builder, List.of(
                compInlineBtn.get("Find word", "/static/findWord"),
                compInlineBtn.get("My dictionary", "/learningWords/dictionary?page=0"),
                compInlineBtn.get("Trainings", "/static/trainings")
        ));
        compInlineMarkup.row(builder, List.of(
                compInlineBtn.get("Help", "/static/help"),
                compInlineBtn.get("Write to the creator ", "/static/creator")
        ));
        return new BotView(compSendMsg.get(session.getId(), "Hello!", builder.build()));
    }

    @BotStaticResource(path = "/static/findWord")
    private BotView findWord(Session session) {
        session.setNextCommand("/words/find?page=0");
        return new BotView(compSendMsg.get(session.getId(), "Enter word or part of it:"));
    }

    @BotStaticResource(path = "/static/creator")
    private BotView writeToTheCreator(Session session) {
        return new BotView(compSendMsg.get(session.getId(), "@TretyakEugeniy"));
    }

    @BotStaticResource(path = "/static/trainings")
    private BotView trainings(Session session) {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder = InlineKeyboardMarkup.builder();
        compInlineMarkup.row(builder,
                compInlineBtn.get("Translate English to Russian", "/static/trainings/select?type=engToRus"));
        compInlineMarkup.row(builder,
                compInlineBtn.get("Translate Russian to English", "/static/trainings/select?type=rusToEng"));
        compInlineMarkup.row(builder, compInlineBtn.get("Go to main menu", "/start"));
        return new BotView(compSendMsg.get(session.getId(), "Choose training:", builder.build()));
    }

    @BotStaticResource(path = "/static/trainings/select")
    private BotView select(Session session) {
        String type = session.getParam("type");
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder = InlineKeyboardMarkup.builder();
        compInlineMarkup.row(builder, List.of(
                compInlineBtn.get("Learn words", String.format("/%s/get?isLearned=false", type)),
                compInlineBtn.get("Repeat words", String.format("/%s/get?isLearned=true", type))
        ));
        compInlineMarkup.row(builder, List.of(
                compInlineBtn.get("Go to back", "/static/trainings"),
                compInlineBtn.get("Go to main menu", "/start")
        ));
        return new BotView(compSendMsg.get(session.getId(), "Choose training:", builder.build()));
    }


    @BotStaticResource(path = "/static/help")
    private BotView helpEn(Session session) {
        String text = "hrlp";
        SendMessage sendMessage = compSendMsg.get(session.getId(), text,
                compInlineMarkup.get(compInlineBtn.get("Go to main menu", "/start")));
        sendMessage.enableHtml(true);
        return new BotView(sendMessage);
    }


}
