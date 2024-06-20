package com.tratsiak.telegram.bot.mvc.lib.core.dispatcher;

import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.lib.core.path.NotValidPathException;
import com.tratsiak.telegram.bot.mvc.lib.core.path.PathValidator;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Getter
@Setter
public abstract class AbstractDispatcherRequests implements DispatcherRequests {

    protected Map<String, MethodOfObject> methodsMap;

    private final PathValidator pathValidator;

    public AbstractDispatcherRequests(PathValidator pathValidator) {
        this.pathValidator = pathValidator;
        this.methodsMap = new HashMap<>();
    }

    @Override
    public BotView executeMethod(String path, Session session) throws ExecuteMethodDispatcherRequestsException {
        MethodOfObject methodOfObject = Optional.ofNullable(methodsMap.get(path)).orElseThrow(
                () -> new ExecuteMethodDispatcherRequestsException(String.format("Endpoint '%s' not found", path))
        );
        try {
            return (BotView) methodOfObject.method.invoke(methodOfObject.object, session);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ExecuteMethodDispatcherRequestsException("Can't invoke method");
        }
    }

    @Override
    public abstract void init(ApplicationContext context);

    protected void put(String finalPath, Method method, Object object) {
        try {
            pathValidator.isValidPath(finalPath);
        } catch (NotValidPathException e) {
            throw new InitializationDispatcherRequestsException("Not valid path", e);
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
