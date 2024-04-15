package com.tratsiak.telegram.bot.mvc.view.util;

import com.tratsiak.telegram.bot.mvc.lib.components.ComponentInlineKeyboardButton;
import com.tratsiak.telegram.bot.mvc.lib.components.ComponentInlineKeyboardMarkup;
import com.tratsiak.telegram.bot.mvc.model.Word;
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
        if (transcription != null) {
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
