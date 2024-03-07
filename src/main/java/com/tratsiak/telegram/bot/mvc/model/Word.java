package com.tratsiak.telegram.bot.mvc.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Word implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private long id;
    private String english;
    private String transcription;
    private String russian;
    private boolean sound;
    @JsonProperty("learningWord")
    private List<LearningWord> learningWords;

    public File getAudio() {
        return Paths.get("D:\\LearningWords\\" + english + ".mp3").toFile();
    }

}