package com.tratsiak.telegram.bot.learning.word.repository.exception;

import lombok.Getter;

@Getter
public class RepositoryException extends RuntimeException {

    private final LevelException levelException;
    private final String publicMessage;


    public RepositoryException(LevelException levelException, String publicMessage, String message) {
        super(message);
        this.levelException = levelException;
        this.publicMessage = publicMessage;
    }

    public RepositoryException(LevelException levelException, String publicMessage, String message, Throwable cause) {
        super(message, cause);
        this.levelException = levelException;
        this.publicMessage = publicMessage;
    }

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
        this.levelException = LevelException.ERROR;
        this.publicMessage = "Error";

    }
}
