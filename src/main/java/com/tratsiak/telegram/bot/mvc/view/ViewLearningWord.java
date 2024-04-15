package com.tratsiak.telegram.bot.mvc.view;

import com.tratsiak.telegram.bot.mvc.lib.components.ComponentInlineKeyboardButton;
import com.tratsiak.telegram.bot.mvc.lib.components.ComponentInlineKeyboardMarkup;
import com.tratsiak.telegram.bot.mvc.lib.components.ComponentSendMessage;
import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.model.LearningWord;
import com.tratsiak.telegram.bot.mvc.model.Page;
import com.tratsiak.telegram.bot.mvc.model.Word;
import com.tratsiak.telegram.bot.mvc.view.util.PageUtil;
import com.tratsiak.telegram.bot.mvc.view.util.WordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class ViewLearningWord {

    private final ComponentSendMessage compSendMsg;
    private final ComponentInlineKeyboardMarkup compInlineMarkup;
    private final ComponentInlineKeyboardButton compInlineBtn;
    private final WordUtil wordUtil;
    private final PageUtil pageUtil;

    @Autowired
    public ViewLearningWord(ComponentSendMessage compSendMsg,
                            ComponentInlineKeyboardMarkup compInlineMarkup,
                            ComponentInlineKeyboardButton compInlineBtn,
                            WordUtil wordUtil, PageUtil pageUtil) {
        this.compSendMsg = compSendMsg;
        this.compInlineMarkup = compInlineMarkup;
        this.compInlineBtn = compInlineBtn;
        this.wordUtil = wordUtil;
        this.pageUtil = pageUtil;
    }

    public BotView delete(long id) {
        return new BotView(compSendMsg.get(id, "Delete"));
    }

    public BotView statistic(long id, Word word) {
        List<LearningWord> learningWords = word.getLearningWords();

        if (learningWords.isEmpty()) {
            return new BotView(compSendMsg.get(id, "Statistic not found"));
        }

        LearningWord learningWord = learningWords.get(0);

        String engToRusDateAsString = "Never";
        String rusToEngDateAsString = "Never";

        Timestamp engToRusDate = learningWord.getTrainingEngToRusDate();
        Timestamp rusToEngDate = learningWord.getTrainingRusToEngDate();
        SimpleDateFormat simpleDateFormat = null;

        if (engToRusDate != null || rusToEngDate != null) {
            simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        }

        if (engToRusDate != null) {
            engToRusDateAsString = simpleDateFormat.format(engToRusDate);
        }

        if (rusToEngDate != null) {
            rusToEngDateAsString = simpleDateFormat.format(rusToEngDate);
        }


        String message = "<b>Translate English to Russian:</b>" +
                "\nNumber of correct answers: " + learningWord.getCountCorrectEngToRus() +
                "\nNumber of incorrect answers: " + learningWord.getCountIncorrectEngToRus() +
                "\nLast training date: " + engToRusDateAsString +
                "\n\n<b>Translate Russian to English:</b>" +
                "\nNumber of correct answers: " + learningWord.getCountCorrectRusToEng() +
                "\nNumber of incorrect answers: " + learningWord.getCountIncorrectRusToEng() +
                "\nLast training date: " + rusToEngDateAsString;

        SendMessage sendMessage = compSendMsg.get(id, message, compInlineMarkup.get(List.of(
                        compInlineBtn.get("Go to back", "/words/get?id=" + word.getId()),
                        compInlineBtn.get("Go to main menu", "/start"))
                )
        );
        sendMessage.enableHtml(true);
        return new BotView(sendMessage);

    }

    public BotView dictionary(long id, Page<LearningWord> wordPage) {

        if (wordPage.getTotalElements() == 0) {
            return new BotView(compSendMsg.get(id,
                    "Dictionary is empty. Find the word you want to learn and add it to your dictionary.",
                    compInlineMarkup.get(
                            List.of(
                                    compInlineBtn.get("Find word", "/static/findWord"),
                                    compInlineBtn.get("Go to main menu", "/start")
                            )
                    )
            ));
        }

        String wordsAsString = "Page not found";
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder = InlineKeyboardMarkup.builder();

        List<LearningWord> learningWords = wordPage.getContent();

        if (!learningWords.isEmpty()) {
            List<Word> words = learningWords.stream().map(LearningWord::getWord).toList();

            wordsAsString = wordUtil.getListWordAsKeyboard(words, builder);
        }

        pageUtil.getNavbar(builder, wordPage, "/learningWords/dictionary?");

        compInlineMarkup.row(builder, compInlineBtn.get("Go to main menu", "/start"));

        SendMessage sendMessage = compSendMsg.get(id, wordsAsString, builder.build());
        sendMessage.enableHtml(true);

        return new BotView(sendMessage);
    }


}
