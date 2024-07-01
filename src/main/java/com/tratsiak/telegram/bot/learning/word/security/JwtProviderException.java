package com.tratsiak.telegram.bot.learning.word.security;

public class JwtProviderException extends RuntimeException {
    public JwtProviderException() {
    }

    public JwtProviderException(String message) {
        super(message);
    }

    public JwtProviderException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtProviderException(Throwable cause) {
        super(cause);
    }

    public JwtProviderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
