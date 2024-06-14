package com.tratsiak.telegram.bot.mvc.lib.core.mapper;

import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.lib.core.path.NotValidPathException;
import com.tratsiak.telegram.bot.mvc.lib.core.path.PathValidator;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


@Getter
@Setter
public abstract class AbstractMethodMapper implements MethodMapper {

    protected Map<String, MethodOfObject> methodsMap;

    private final PathValidator pathValidator;

    public AbstractMethodMapper(PathValidator pathValidator) {
        this.pathValidator = pathValidator;
        this.methodsMap = new HashMap<>();
    }

    @Override
    public BotView executeMethod(String path, Session session) throws MethodMapperException {
        MethodOfObject methodOfObject = methodsMap.get(path);

        if (methodOfObject == null) {
            return null;
        }

        BotView botView;

        try {
            botView = (BotView) methodOfObject.method.invoke(methodOfObject.object, session);
        } catch (IllegalAccessException | InvocationTargetException e) {
            botView = exceptionHandler(e, methodOfObject.method, session);
        }
        return botView;
    }

    @Override
    public abstract void init(ApplicationContext context);

    protected abstract BotView exceptionHandler(Exception e, Method method, Session session) throws MethodMapperException;

    protected void put(String finalPath, Method method, Object object) {
        try {
            pathValidator.isValidPath(finalPath);
        } catch (NotValidPathException e) {
            throw new RuntimeException(e);
        }
        method.setAccessible(true);
        methodsMap.put(finalPath, new MethodOfObject(object, method));
    }

    @AllArgsConstructor
    protected static class MethodOfObject {
        private Object object;
        private Method method;
    }
}
