package com.tratsiak.telegram.bot.mvc.lib.exception;

import com.tratsiak.telegram.bot.mvc.lib.core.View;
import lombok.Getter;

@Getter
public class ResponseException extends RuntimeException {

    private View view;

    public ResponseException(String message) {
        super(message);
    }

    public ResponseException(View view) {
        this.view = view;
    }

}
