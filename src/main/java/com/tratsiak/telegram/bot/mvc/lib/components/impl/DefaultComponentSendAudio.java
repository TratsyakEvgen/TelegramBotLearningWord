package com.tratsiak.telegram.bot.mvc.lib.components.impl;

import com.tratsiak.telegram.bot.mvc.lib.components.ComponentSendAudio;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;

@Component
public class DefaultComponentSendAudio implements ComponentSendAudio {
    @Override
    public SendAudio get(long id, File file) {
        return SendAudio.builder()
                .audio(new InputFile(file))
                .chatId(id)
                .build();
    }

}
