package com.tratsiak.telegram.bot.learning.word.view.util;


import com.tratsiak.telegram.bot.learning.word.model.Word;
import com.tratsiak.telegrambotmvc.components.ComponentInlineKeyboardButton;
import com.tratsiak.telegrambotmvc.components.ComponentInlineKeyboardMarkup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

@Component
public class WordUtil {

    private final ComponentInlineKeyboardButton compInlineBtn;
    private final ComponentInlineKeyboardMarkup compInlineMarkup;

    @Autowired
    public WordUtil(ComponentInlineKeyboardButton compInlineBtn, ComponentInlineKeyboardMarkup compInlineMarkup) {
        this.compInlineBtn = compInlineBtn;
        this.compInlineMarkup = compInlineMarkup;
    }

    public String getWordAsSting(Word word) {
        String string = "<b>English: </b>" + word.getEnglish();

        String transcription = word.getTranscription();
        if (!transcription.isEmpty()) {
            string = string + "\n<b>Transcription: </b>" + transcription;
        }

        string = string + "\n<b>Russian: </b>" + word.getRussian();
        return string;
    }

    public String getListWordAsKeyboard(List<Word> words, InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder) {
        StringBuilder stringBuilder = new StringBuilder();
        words.forEach(word -> {
            stringBuilder.append(getWordAsSting(word));
            stringBuilder.append("\n\n");
            compInlineMarkup.row(builder, compInlineBtn.get(word.getEnglish(), "/words/get?id=" + word.getId()));
        });
        return stringBuilder.toString();
    }
}
