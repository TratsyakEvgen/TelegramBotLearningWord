package com.tratsiak.telegram.bot.mvc.repository;

import com.tratsiak.telegram.bot.mvc.repository.exception.RepositoryException;

import java.io.File;

public interface AudioRepository {

    File getAudioFile(String word) throws RepositoryException;
}
