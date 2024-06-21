package com.tratsiak.telegram.bot.mvc.lib.core;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.util.List;

public interface View {
    List<PartialBotApiMethod<?>> getMessages();
}
