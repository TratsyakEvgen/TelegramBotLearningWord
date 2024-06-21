package com.tratsiak.telegram.bot.mvc.lib.core;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@EqualsAndHashCode
@ToString
public class BotView implements View {

    private List<PartialBotApiMethod<?>> messages;

    public BotView(PartialBotApiMethod<?> message) {
        this.messages = new ArrayList<>();
        messages.add(message);
    }

    public void put(PartialBotApiMethod<?> message) {
        messages.add(message);
    }

    @Override
    public List<PartialBotApiMethod<?>> getMessages() {
        return messages;
    }
}
