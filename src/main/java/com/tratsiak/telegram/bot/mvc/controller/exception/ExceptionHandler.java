package com.tratsiak.telegram.bot.mvc.controller.exception;

import com.tratsiak.telegram.bot.mvc.lib.core.mapper.ResponseException;
import com.tratsiak.telegram.bot.mvc.repository.exception.RepositoryException;

public interface ExceptionHandler {

    ResponseException handle(RepositoryException e);
}
