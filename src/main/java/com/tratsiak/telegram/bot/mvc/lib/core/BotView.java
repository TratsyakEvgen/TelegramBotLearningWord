package com.tratsiak.telegram.bot.mvc.lib.core;

import lombok.*;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class BotView {

    private List<PartialBotApiMethod<?>> sendingMessages = new ArrayList<>();

    public BotView(PartialBotApiMethod<?> message) {
        sendingMessages.add(message);
    }

    public void put(PartialBotApiMethod<?> message) {
        sendingMessages.add(message);
    }

}
