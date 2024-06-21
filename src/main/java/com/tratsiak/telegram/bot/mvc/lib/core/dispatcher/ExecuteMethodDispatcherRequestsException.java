package com.tratsiak.telegram.bot.mvc.lib.core.dispatcher;

public class ExecuteMethodDispatcherRequestsException extends RuntimeException {

    public ExecuteMethodDispatcherRequestsException() {
    }

    public ExecuteMethodDispatcherRequestsException(String message) {
        super(message);
    }

    public ExecuteMethodDispatcherRequestsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecuteMethodDispatcherRequestsException(Throwable cause) {
        super(cause);
    }

    public ExecuteMethodDispatcherRequestsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
