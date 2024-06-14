package com.tratsiak.telegram.bot.mvc.lib.core.path;

public class NotValidPathException extends Exception {

    public NotValidPathException() {
    }

    public NotValidPathException(String message) {
        super(message);
    }

    public NotValidPathException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotValidPathException(Throwable cause) {
        super(cause);
    }

    public NotValidPathException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
