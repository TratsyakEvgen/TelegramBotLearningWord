package com.tratsiak.telegram.bot.mvc.lib.core.path;

public interface PathValidator {
    void isValidPath(String path) throws NotValidPathException;

    void isValidPathWithParams(String path) throws NotValidPathException;
}
