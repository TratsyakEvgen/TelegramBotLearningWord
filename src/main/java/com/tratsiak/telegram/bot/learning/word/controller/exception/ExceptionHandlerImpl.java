package com.tratsiak.telegram.bot.learning.word.controller.exception;

import com.tratsiak.telegram.bot.learning.word.repository.exception.LevelException;
import com.tratsiak.telegram.bot.learning.word.repository.exception.RepositoryException;
import com.tratsiak.telegrambotmvc.exception.ResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@Slf4j
public class ExceptionHandlerImpl implements ExceptionHandler {
    @Override
    public ResponseException handle(RepositoryException e) throws ResponseStatusException {

        LevelException levelException = e.getLevelException();

        if (levelException.equals(LevelException.INFO)) {
            log.info(e.getMessage());
            return new ResponseException(e.getPublicMessage());
        }

        if (levelException.equals(LevelException.WARM)) {
            log.warn(e.getMessage());
            return new ResponseException(e.getPublicMessage());
        }

        if (levelException.equals(LevelException.ERROR)) {
            log.error(e.getMessage(), e);
            return new ResponseException(e.getPublicMessage());
        }

        return null;
    }
}
