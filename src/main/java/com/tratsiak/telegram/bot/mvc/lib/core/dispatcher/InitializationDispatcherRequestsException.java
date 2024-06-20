package com.tratsiak.telegram.bot.mvc.lib.core.dispatcher;

public class InitializationDispatcherRequestsException extends RuntimeException {

    public InitializationDispatcherRequestsException() {
    }

    public InitializationDispatcherRequestsException(String message) {
        super(message);
    }

    public InitializationDispatcherRequestsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InitializationDispatcherRequestsException(Throwable cause) {
        super(cause);
    }

    public InitializationDispatcherRequestsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
