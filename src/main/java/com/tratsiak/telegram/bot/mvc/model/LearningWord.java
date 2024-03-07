package com.tratsiak.telegram.bot.mvc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class LearningWord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private long id;
    private Word word;
    private int countCorrectEngToRus;
    private int countIncorrectEngToRus;
    private Timestamp trainingEngToRusDate;
    private int countCorrectRusToEng;
    private int countIncorrectRusToEng;
    private Timestamp trainingRusToEngDate;
    private boolean learnedStatus;


}
