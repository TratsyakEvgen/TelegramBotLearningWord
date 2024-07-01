package com.tratsiak.telegram.bot.learning.word.repository.impl;

import com.tratsiak.telegram.bot.learning.word.repository.AudioRepository;
import com.tratsiak.telegram.bot.learning.word.repository.exception.LevelException;
import com.tratsiak.telegram.bot.learning.word.repository.exception.RepositoryException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

@Repository
public class AudioRepositoryImpl implements AudioRepository {

    private final String audio;

    public AudioRepositoryImpl(@Value("${audio}") String audio) {
        this.audio = audio;
    }

    @Override
    public File getAudioFile(String word) throws RepositoryException {
        try {
            return Paths.get(audio + word + ".mp3").toFile();
        } catch (InvalidPathException e) {
            throw new RepositoryException(LevelException.WARM, "Sound not found ", "Can't get audio file " + word, e);
        }

    }
}
