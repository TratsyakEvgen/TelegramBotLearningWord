package com.tratsiak.telegram.bot.mvc.lib.core.mapper;

import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import com.tratsiak.telegram.bot.mvc.lib.util.BotPath;
import com.tratsiak.telegram.bot.mvc.lib.util.NotValidPathException;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;


@Getter
@Setter
public abstract class AbstractMapper implements Mapper {

    protected Map<String, MethodOfObject> methodsMap;

    @Override
    public BotView executeMethod(String path, Session session) throws MapperException {
        MethodOfObject methodOfObject = methodsMap.get(path);

        if (methodOfObject == null) {
            return null;
        }

        try {
            return (BotView) methodOfObject.method.invoke(methodOfObject.object, session);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new MapperException("Can't execute method " + methodOfObject.method, e);
        }

    }

    @PostConstruct
    protected abstract void init();

    protected void put(String finalPath, Method method, Object object) {
        try {
            BotPath.validatePath(finalPath);
        } catch (NotValidPathException e) {
            throw new RuntimeException(e);
        }
        method.setAccessible(true);
        methodsMap.put(finalPath, new MethodOfObject(object, method));
    }

    @AllArgsConstructor
    public static class MethodOfObject {
        private Object object;
        private Method method;

    }
}
