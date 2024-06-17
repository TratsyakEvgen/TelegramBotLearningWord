package com.tratsiak.telegram.bot.mvc.lib.core.exeption.handler.impl;

import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.lib.core.exeption.handler.ExceptionHandler;
import com.tratsiak.telegram.bot.mvc.lib.core.exeption.handler.MapperExceptionHandler;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

@Component
public class DefaultMapperExceptionHandlers implements MapperExceptionHandler {

    private final ApplicationContext context;
    private Map<Class<?>, ExceptionHandler<? extends Exception>> handlerMap;


    @Autowired
    public DefaultMapperExceptionHandlers(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void init() {
        Map<String, ExceptionHandler> beansOfType = context.getBeansOfType(ExceptionHandler.class);
        beansOfType.forEach((string, exceptionHandler) -> {
                    Type[] genericInterfaces = exceptionHandler.getClass().getGenericInterfaces();
                    for (Type genericInterface : genericInterfaces) {
                        if (genericInterface instanceof ParameterizedType) {
                            Type genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
                            handlerMap.put((Class<?>) genericTypes, exceptionHandler);
                        }
                    }
                }
        );
    }

    @Override
    public BotView handle(Exception e, Session session) {
        Throwable cause = e;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }


        Throwable finalCause = cause;
        return handlerMap.entrySet()
                .stream()
                .filter((entry -> entry.getKey().isInstance(finalCause.getClass())))
                .findFirst()
                .map(classExceptionHandlerEntry -> classExceptionHandlerEntry.getValue().handle(e, session))
                .orElse(null);


    }

}
