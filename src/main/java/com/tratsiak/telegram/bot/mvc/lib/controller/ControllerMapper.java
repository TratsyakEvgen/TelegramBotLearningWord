package com.tratsiak.telegram.bot.mvc.lib.controller;

import com.tratsiak.telegram.bot.mvc.lib.controller.BotController;
import com.tratsiak.telegram.bot.mvc.lib.controller.BotRequestMapping;
import com.tratsiak.telegram.bot.mvc.lib.util.BotPath;
import com.tratsiak.telegram.bot.mvc.lib.util.NotValidPathException;
import com.tratsiak.telegram.bot.mvc.lib.view.BotView;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class ControllerMapper {

    private final ApplicationContext context;

    @Getter
    private Map<String, MethodOfObject> methodsMap;

    @Autowired
    public ControllerMapper(ApplicationContext context) {
        this.context = context;
    }


    @PostConstruct
    private void init() {
        Map<String, Object> mapControllerBeans = context.getBeansWithAnnotation(BotController.class);

        if (!mapControllerBeans.isEmpty()) {
            methodsMap = new HashMap<>();
        }

        mapControllerBeans.keySet().forEach(bean -> {

                    BotRequestMapping annatationBotRequestMapping =
                            context.findAnnotationOnBean(bean, BotRequestMapping.class);

                    String path;
                    if (annatationBotRequestMapping != null) {
                        path = annatationBotRequestMapping.path();
                    } else {
                        path = "";
                    }

                    Object object = mapControllerBeans.get(bean);

                    Arrays.stream(object.getClass().getDeclaredMethods()).forEach(method -> {

                                BotRequestMapping methodRequestMapping =
                                        method.getDeclaredAnnotation(BotRequestMapping.class);

                                if (methodRequestMapping != null) {
                                    String finalPath = path + methodRequestMapping.path();
                                    try {
                                        BotPath.validatePath(finalPath);
                                    } catch (NotValidPathException e) {
                                        throw new RuntimeException(e);
                                    }
                                    method.setAccessible(true);
                                    methodsMap.put(finalPath, new MethodOfObject(object, method));

                                }
                            }
                    );
                }
        );

    }

    public BotView executeControllerMethod(String path) throws ControllerMapperException {
        MethodOfObject methodOfObject = methodsMap.get(path);
        if (methodOfObject == null){
            throw new ControllerMapperException("Path" + path + "not found");
        }
        try {
            return (BotView) methodOfObject.method.invoke(methodOfObject.object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ControllerMapperException("Can't execute method " + methodOfObject.method, e);
        }

    }


    @AllArgsConstructor
    private static class MethodOfObject {
        private Object object;
        private Method method;

    }
}
