package com.tratsiak.telegram.bot.mvc.model;

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
public class TrainingTranslateEngToRus implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private long learningWordId;
    private String translatedWord;
    private List<Word> options;
    private long answer;
    private String transcription;
    private boolean sound;
}
