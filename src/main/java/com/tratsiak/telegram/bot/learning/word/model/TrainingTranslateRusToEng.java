package com.tratsiak.telegram.bot.learning.word.model;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class TrainingTranslateRusToEng implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private long learningWordId;
    private String translatedWord;
    private List<Word> options;
    private long answer;

}
