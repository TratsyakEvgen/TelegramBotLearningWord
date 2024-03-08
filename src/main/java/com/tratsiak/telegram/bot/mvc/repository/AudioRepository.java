package com.tratsiak.telegram.bot.mvc.repository;

import java.io.File;

public interface AudioRepository {

    File getAudioFile(String word) throws RepositoryException;
}
