package com.tratsiak.telegram.bot.mvc.model.bean;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ErrorResponse {
    private Timestamp timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
