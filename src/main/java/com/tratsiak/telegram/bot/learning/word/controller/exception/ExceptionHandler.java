package com.tratsiak.telegram.bot.learning.word.controller.exception;


import com.tratsiak.telegram.bot.learning.word.repository.exception.RepositoryException;
import com.tratsiak.telegrambotmvc.exception.ResponseException;

public interface ExceptionHandler {

    ResponseException handle(RepositoryException e);
}
