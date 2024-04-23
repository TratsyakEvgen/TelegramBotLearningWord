package com.tratsiak.telegram.bot.mvc.view;

import com.tratsiak.telegram.bot.mvc.lib.components.ComponentInlineKeyboardButton;
import com.tratsiak.telegram.bot.mvc.lib.components.ComponentInlineKeyboardMarkup;
import com.tratsiak.telegram.bot.mvc.lib.components.ComponentSendMessage;
import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.model.TrainingTranslateEngToRus;
import com.tratsiak.telegram.bot.mvc.model.TrainingTranslateRusToEng;
import com.tratsiak.telegram.bot.mvc.model.Word;
import com.tratsiak.telegram.bot.mvc.view.util.WordUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class TrainingTranslate {

    private final ComponentSendMessage compSendMsg;
    private final ComponentInlineKeyboardButton compInlineBtn;
    private final ComponentInlineKeyboardMarkup compInlineMarkup;

    private final WordUtil wordUtil;

    public TrainingTranslate(ComponentSendMessage compSendMsg,
                             ComponentInlineKeyboardButton compInlineBtn,
                             ComponentInlineKeyboardMarkup compInlineMarkup, WordUtil wordUtil) {
        this.compSendMsg = compSendMsg;
        this.compInlineBtn = compInlineBtn;
        this.compInlineMarkup = compInlineMarkup;
        this.wordUtil = wordUtil;
    }

    public BotView training(long userId, TrainingTranslateEngToRus engToRus, boolean status) {

        StringBuilder builder = new StringBuilder();
        String translatedWord = engToRus.getTranslatedWord();
        builder.append("<b>Word: </b>").append(translatedWord).append("\n");


        String transcription = engToRus.getTranscription();
        if (transcription != null) {
            builder.append("<b>transcription: </b>").append(transcription).append("\n\n");
        }

        List<InlineKeyboardButton> buttons = new ArrayList<>();

        int number = 1;
        long id = engToRus.getLearningWordId();

        for (Word word : engToRus.getOptions()) {
            builder.append("<b>").append(number).append(". </b>").append(word.getRussian()).append("\n\n");
            buttons.add(compInlineBtn.get(String.valueOf(number),
                    String.format("/engToRus/check?id=%d&ans=%d&status=%b", id, word.getId(), status)
            ));
            number++;
        }

        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder markupBuilder = InlineKeyboardMarkup.builder();
        compInlineMarkup.row(markupBuilder, buttons);

        if (engToRus.isSound()) {
            compInlineMarkup.row(markupBuilder,
                    compInlineBtn.get("Sound", "/words/getAudio?file=" + translatedWord)
            );
        }

        String callback = String.format("/engToRus/update?status=%b&id=%d", !status, id);

        return getBotView(userId, status, builder, markupBuilder, callback);
    }


    public BotView training(long userId, TrainingTranslateRusToEng rusToEng, boolean status) {

        StringBuilder builder = new StringBuilder();
        String translatedWord = rusToEng.getTranslatedWord();
        builder.append("<b>Word: </b>").append(translatedWord).append("\n\n");

        List<InlineKeyboardButton> buttons = new ArrayList<>();

        int number = 1;
        long id = rusToEng.getLearningWordId();

        for (Word word : rusToEng.getOptions()) {
            builder.append("<b>").append(number).append(". </b>").append(word.getEnglish()).append("\n");

            String transcription = word.getTranscription();
            if (transcription != null) {
                builder.append(transcription).append("\n");
            }

            builder.append("\n");

            buttons.add(compInlineBtn.get(String.valueOf(number),
                    String.format("/rusToEng/check?id=%d&ans=%d&status=%b", id, word.getId(), status)
            ));
            number++;
        }
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder markupBuilder = InlineKeyboardMarkup.builder();
        compInlineMarkup.row(markupBuilder, buttons);

        String callback =String.format("/rusToEng/update?status=%b&id=%d", !status, id);

        return getBotView(userId, status, builder, markupBuilder, callback);
    }

    public BotView mistake(long userId, Word word, String path) {
        String text = "<b>You are mistaken!</b>\nCorrect option:\n\n" + wordUtil.getWordAsSting(word);
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder = InlineKeyboardMarkup.builder();
        compInlineMarkup.row(builder, compInlineBtn.get("Next", path));
        compInlineMarkup.row(builder, List.of(
                compInlineBtn.get("Go to back", "/static/trainings"),
                compInlineBtn.get("Go to main menu", "/start")
        ));
        SendMessage sendMessage = compSendMsg.get(userId, text, builder.build());
        sendMessage.enableHtml(true);

        return new BotView(sendMessage);
    }


    private BotView getBotView(long userId,
                               boolean status,
                               StringBuilder builder,
                               InlineKeyboardMarkup.InlineKeyboardMarkupBuilder markupBuilder,
                               String callback) {

        String isLearned = status ? "Mark as unlearned" : "Mark as learned";

        compInlineMarkup.row(markupBuilder, compInlineBtn.get(isLearned, callback));

        compInlineMarkup.row(markupBuilder, List.of(
                compInlineBtn.get("Go to back", "/static/trainings"),
                compInlineBtn.get("Go to main menu", "/start")
        ));

        SendMessage sendMessage = compSendMsg.get(userId, builder.toString(), markupBuilder.build());
        sendMessage.enableHtml(true);

        return new BotView(sendMessage);
    }
}
