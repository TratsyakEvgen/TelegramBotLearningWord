package com.tratsiak.telegram.bot.mvc.lib.core.mapper;

import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import lombok.Getter;

@Getter
public class ResponseException extends RuntimeException {

    private BotView botView;

    public ResponseException(String message) {
        super(message);
    }

    public ResponseException(BotView botView) {
        super();
        this.botView = botView;
    }

}
