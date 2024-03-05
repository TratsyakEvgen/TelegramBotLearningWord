package com.tratsiak.telegram.bot.mvc.lib.session;

public class BotSessionException extends Exception {
    public BotSessionException() {
    }

    public BotSessionException(String message) {
        super(message);
    }

    public BotSessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public BotSessionException(Throwable cause) {
        super(cause);
    }

    public BotSessionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
