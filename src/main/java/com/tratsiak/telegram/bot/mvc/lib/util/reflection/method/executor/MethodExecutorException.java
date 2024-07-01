package com.tratsiak.telegram.bot.mvc.lib.util.reflection.method.executor;

public class MethodExecutorException extends RuntimeException {
    public MethodExecutorException() {
    }

    public MethodExecutorException(String message) {
        super(message);
    }

    public MethodExecutorException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodExecutorException(Throwable cause) {
        super(cause);
    }

    public MethodExecutorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
