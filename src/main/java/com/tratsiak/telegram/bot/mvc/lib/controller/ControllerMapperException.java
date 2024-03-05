package com.tratsiak.telegram.bot.mvc.lib.controller;

public class ControllerMapperException extends Exception{

    public ControllerMapperException() {
    }

    public ControllerMapperException(String message) {
        super(message);
    }

    public ControllerMapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public ControllerMapperException(Throwable cause) {
        super(cause);
    }

    public ControllerMapperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
