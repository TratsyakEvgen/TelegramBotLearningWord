package com.tratsiak.telegram.bot.learning.word.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Page<T> {
    @JsonProperty("content")
    private List<T> content;
    @JsonProperty("number")
    private int number;
    @JsonProperty("size")
    private int size;
    @JsonProperty("totalElements")
    private long totalElements;
    @JsonProperty("empty")
    private boolean empty;
    @JsonProperty("first")
    private boolean first;
    @JsonProperty("last")
    private boolean last;
    @JsonProperty("totalPages")
    private int totalPages;
    @JsonProperty("numberOfElements")
    private int numberOfElements;


}
