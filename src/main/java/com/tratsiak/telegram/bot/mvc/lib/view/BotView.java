package com.tratsiak.telegram.bot.mvc.lib.view;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;


import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@EqualsAndHashCode
@ToString
public class BotView {

    private List<PartialBotApiMethod<?>> sendingMessages = new ArrayList<>();

    public void put(PartialBotApiMethod<?> message) {
        sendingMessages.add(message);
    }

}
