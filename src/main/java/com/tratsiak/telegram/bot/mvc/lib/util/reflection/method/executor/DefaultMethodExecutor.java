package com.tratsiak.telegram.bot.mvc.lib.util.reflection.method.executor;


import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

@Component
public class DefaultMethodExecutor implements MethodExecutor {
    @Override
    public void executeVoidMethodWithParameter(Object object, Object param, String paramName) throws MethodExecutorException {
        Class<?> paramClass = param.getClass();
        try {
            Arrays.stream(object.getClass().getMethods())
                    .filter(method -> method.getName().equals(paramName))
                    .filter(method -> Arrays.stream(method.getParameterTypes())
                            .allMatch(p -> p.equals(paramClass) | p.isAssignableFrom(paramClass)))
                    .findFirst()
                    .orElseThrow(() -> new MethodExecutorException("Method execute(" + paramClass + ") not found"))
                    .invoke(object, param);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new MethodExecutorException(
                    String.format("Can't invoke method %s with parameter %s", object.getClass().getName(), param)
            );
        }
    }
}
