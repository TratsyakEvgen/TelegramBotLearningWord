package com.tratsiak.telegram.bot.mvc.view;

import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.model.LearningWord;
import com.tratsiak.telegram.bot.mvc.model.Word;
import com.tratsiak.telegram.bot.mvc.view.components.ComponentInlineKeyboardButton;
import com.tratsiak.telegram.bot.mvc.view.components.ComponentInlineKeyboardMarkup;
import com.tratsiak.telegram.bot.mvc.view.components.ComponentSendAudio;
import com.tratsiak.telegram.bot.mvc.view.components.ComponentSendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

@Component
public class ViewWord {

    private final ComponentSendMessage compSendMsg;
    private final ComponentInlineKeyboardButton compInlineBtn;
    private final ComponentInlineKeyboardMarkup compInlineMarkup;
    private final ComponentSendAudio compSendAudio;


    @Autowired
    public ViewWord(ComponentSendMessage compSendMsg,
                    ComponentInlineKeyboardButton compInlineBtn,
                    ComponentInlineKeyboardMarkup compInlineMarkup,
                    ComponentSendAudio compSendAudio) {
        this.compSendMsg = compSendMsg;
        this.compInlineBtn = compInlineBtn;
        this.compInlineMarkup = compInlineMarkup;
        this.compSendAudio = compSendAudio;

    }

    public BotView findWords(long id, List<Word> words) {

        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder = InlineKeyboardMarkup.builder();
        StringBuilder stringBuilder = new StringBuilder();
        words.forEach(word -> {
            stringBuilder.append(wordToString(word));
            stringBuilder.append("\n\n");
            compInlineMarkup.row(builder, compInlineBtn.get(word.getEnglish(),
                    "/words/get?id=" + word.getId()));
        });

        SendMessage sendMessage = compSendMsg.get(id, stringBuilder.toString(), builder.build());
        sendMessage.enableHtml(true);

        return new BotView(sendMessage);

    }

    public BotView word(long id, Word word) {

        List<LearningWord> learningWords = word.getLearningWords();
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder markupBuilder = InlineKeyboardMarkup.builder();
        long wordId = word.getId();

        if (!learningWords.isEmpty()) {
            LearningWord learningWord = learningWords.get(0);
            boolean status = learningWord.isLearnedStatus();
            long learningWordId = learningWord.getId();

            String isLearned = status ? "Mark as unlearned" : "Mark as learned";
            String callback = status ? "/learningWords/update?status=false&id=" + learningWordId
                    : "/learningWords/update?status=true&id=" + learningWordId;
            compInlineMarkup.row(markupBuilder, List.of(compInlineBtn.get(isLearned, callback),
                            compInlineBtn.get("Delete", "/learningWords/delete?id=" + learningWordId)
                    )
            );
            compInlineMarkup.row(markupBuilder,
                    compInlineBtn.get("Statistic", "/learningWords/statistic?id=" + learningWordId)
            );

        } else {
            compInlineMarkup.row(markupBuilder, compInlineBtn.get("Add", "/learningWords/add?id=" + wordId));
        }


        if (word.isSound()) {
            compInlineMarkup.row(markupBuilder, compInlineBtn.get("Sound", "/words/getAudio?id=" + wordId));
        }

        compInlineMarkup.row(markupBuilder, compInlineBtn.get("Back", "/start"));

        SendMessage sendMessage = compSendMsg.get(id, wordToString(word), markupBuilder.build());
        sendMessage.enableHtml(true);

        return new BotView(sendMessage);
    }

    public BotView sound(long id, Word word) {
        return new BotView(compSendAudio.get(id, word.getAudio()));
    }

    private String wordToString(Word word) {
        StringBuilder builder = new StringBuilder();
        builder.append("<b>English: </b>");
        builder.append(word.getEnglish());

        String transcription = word.getTranscription();
        if (transcription != null) {
            builder.append("\n<b>Transcription: </b>");
            builder.append(transcription);
        }

        builder.append("\n<b>Russian: </b>");
        builder.append(word.getRussian());
        return builder.toString();
    }

}
