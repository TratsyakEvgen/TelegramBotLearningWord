package com.tratsiak.telegram.bot.mvc.lib.core.mapper;

public class MethodMapperException extends Exception {

    public MethodMapperException() {
    }

    public MethodMapperException(String message) {
        super(message);
    }

    public MethodMapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodMapperException(Throwable cause) {
        super(cause);
    }

    public MethodMapperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
