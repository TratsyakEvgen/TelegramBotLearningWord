package com.tratsiak.telegram.bot.mvc.view;

import com.tratsiak.telegram.bot.mvc.lib.components.ComponentInlineKeyboardButton;
import com.tratsiak.telegram.bot.mvc.lib.components.ComponentInlineKeyboardMarkup;
import com.tratsiak.telegram.bot.mvc.lib.components.ComponentSendAudio;
import com.tratsiak.telegram.bot.mvc.lib.components.ComponentSendMessage;
import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.model.LearningWord;
import com.tratsiak.telegram.bot.mvc.model.Word;
import com.tratsiak.telegram.bot.mvc.view.util.WordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.File;
import java.util.List;

@Component
public class ViewWord {

    private final ComponentSendMessage compSendMsg;
    private final ComponentInlineKeyboardButton compInlineBtn;
    private final ComponentInlineKeyboardMarkup compInlineMarkup;
    private final ComponentSendAudio compSendAudio;

    private final WordUtil wordUtil;


    @Autowired
    public ViewWord(ComponentSendMessage compSendMsg,
                    ComponentInlineKeyboardButton compInlineBtn,
                    ComponentInlineKeyboardMarkup compInlineMarkup,
                    ComponentSendAudio compSendAudio,
                    WordUtil wordUtil) {
        this.compSendMsg = compSendMsg;
        this.compInlineBtn = compInlineBtn;
        this.compInlineMarkup = compInlineMarkup;
        this.compSendAudio = compSendAudio;
        this.wordUtil = wordUtil;
    }

    public BotView findWords(long id, List<Word> words) {

        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder = InlineKeyboardMarkup.builder();
        String wordsAsString = wordUtil.getListWordAsKeyboard(words, builder);

        compInlineMarkup.row(builder, compInlineBtn.get("Go to back", "/start"));

        SendMessage sendMessage = compSendMsg.get(id, wordsAsString, builder.build());
        sendMessage.enableHtml(true);

        return new BotView(sendMessage);

    }

    public BotView word(long id, Word word) {

        List<LearningWord> learningWords = word.getLearningWords();
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder = InlineKeyboardMarkup.builder();
        long wordId = word.getId();

        if (!learningWords.isEmpty()) {
            LearningWord learningWord = learningWords.get(0);
            boolean status = learningWord.isLearnedStatus();
            long learningWordId = learningWord.getId();

            String isLearned = status ? "Mark as unlearned" : "Mark as learned";
            String callback = status ? "/learningWords/update?status=false&id=" + learningWordId
                    : "/learningWords/update?status=true&id=" + learningWordId;
            compInlineMarkup.row(builder, List.of(compInlineBtn.get(isLearned, callback),
                            compInlineBtn.get("Delete", "/learningWords/delete?id=" + learningWordId)
                    )
            );
            compInlineMarkup.row(builder,
                    compInlineBtn.get("Statistic", "/learningWords/statistic?id=" + wordId)
            );

        } else {
            compInlineMarkup.row(builder, compInlineBtn.get("Add to dictionary", "/learningWords/add?id=" + wordId));
        }


        if (word.isSound()) {
            compInlineMarkup.row(builder, compInlineBtn.get("Sound", "/words/getAudio?id=" + wordId));
        }

        compInlineMarkup.row(builder, compInlineBtn.get("Go to back", "/start"));

        SendMessage sendMessage = compSendMsg.get(id, wordUtil.getWordAsSting(word), builder.build());
        sendMessage.enableHtml(true);

        return new BotView(sendMessage);
    }

    public BotView sound(long id, File file) {
        return new BotView(compSendAudio.get(id, file));
    }


}
