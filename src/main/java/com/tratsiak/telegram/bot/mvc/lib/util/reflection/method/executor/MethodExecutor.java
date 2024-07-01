package com.tratsiak.telegram.bot.mvc.lib.util.reflection.method.executor;

public interface MethodExecutor {
    void executeVoidMethodWithParameter(Object object, Object param, String paramName);
}
