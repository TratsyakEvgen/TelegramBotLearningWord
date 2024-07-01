package com.tratsiak.telegram.bot.learning.word.repository;

import com.tratsiak.telegram.bot.learning.word.repository.exception.RepositoryException;

import java.io.File;

public interface AudioRepository {

    File getAudioFile(String word) throws RepositoryException;
}
