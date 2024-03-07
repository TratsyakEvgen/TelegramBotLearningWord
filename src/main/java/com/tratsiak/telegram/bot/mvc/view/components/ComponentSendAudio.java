package com.tratsiak.telegram.bot.mvc.view.components;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.net.URISyntaxException;

@Component
public class ComponentSendAudio {

    public SendAudio get(long id, File file){
        return SendAudio.builder()
                .audio(new InputFile(file))
                .chatId(id)
                .build();
    }

}
