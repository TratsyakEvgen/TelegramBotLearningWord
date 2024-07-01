package com.tratsiak.telegram.bot.learning.word.view;


import com.tratsiak.telegram.bot.learning.word.model.LearningWord;
import com.tratsiak.telegram.bot.learning.word.model.Page;
import com.tratsiak.telegram.bot.learning.word.model.Word;
import com.tratsiak.telegram.bot.learning.word.view.util.PageUtil;
import com.tratsiak.telegram.bot.learning.word.view.util.WordUtil;
import com.tratsiak.telegrambotmvc.components.ComponentInlineKeyboardButton;
import com.tratsiak.telegrambotmvc.components.ComponentInlineKeyboardMarkup;
import com.tratsiak.telegrambotmvc.components.ComponentSendAudio;
import com.tratsiak.telegrambotmvc.components.ComponentSendMessage;
import com.tratsiak.telegrambotmvc.core.view.BotView;
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
    private final PageUtil pageUtil;


    @Autowired
    public ViewWord(ComponentSendMessage compSendMsg,
                    ComponentInlineKeyboardButton compInlineBtn,
                    ComponentInlineKeyboardMarkup compInlineMarkup,
                    ComponentSendAudio compSendAudio,
                    WordUtil wordUtil,
                    PageUtil pageUtil) {
        this.compSendMsg = compSendMsg;
        this.compInlineBtn = compInlineBtn;
        this.compInlineMarkup = compInlineMarkup;
        this.compSendAudio = compSendAudio;
        this.wordUtil = wordUtil;
        this.pageUtil = pageUtil;
    }

    public BotView findWords(long id, String part, Page<Word> wordPage) {

        if (wordPage.getTotalElements() == 0) {
            return new BotView(compSendMsg.get(id, "Not found"));
        }

        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder = InlineKeyboardMarkup.builder();
        String wordsAsString = wordUtil.getListWordAsKeyboard(wordPage.getContent(), builder);

        pageUtil.getNavbar(builder, wordPage, String.format("/words/find?part=%s&", part));

        compInlineMarkup.row(builder, compInlineBtn.get("Go to main menu", "/start"));

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

            String callback = String.format("/learningWords/update?status=%b&id=%d", !status, learningWordId);

            compInlineMarkup.row(builder, List.of(
                    compInlineBtn.get(isLearned, callback),
                    compInlineBtn.get("Delete", "/learningWords/delete?id=" + learningWordId)
            ));

            compInlineMarkup.row(builder,
                    compInlineBtn.get("Statistic", "/learningWords/statistic?id=" + wordId)
            );

        } else {
            compInlineMarkup.row(builder, compInlineBtn.get("Add to dictionary", "/learningWords/add?id=" + wordId));
        }


        if (word.isSound()) {
            compInlineMarkup.row(builder, compInlineBtn.get("Sound", "/words/getAudio?file=" + word.getEnglish()));
        }

        compInlineMarkup.row(builder, compInlineBtn.get("Go to main menu", "/start"));

        SendMessage sendMessage = compSendMsg.get(id, wordUtil.getWordAsSting(word), builder.build());
        sendMessage.enableHtml(true);

        return new BotView(sendMessage);
    }

    public BotView sound(long id, File file) {
        return new BotView(compSendAudio.get(id, file));
    }


}
