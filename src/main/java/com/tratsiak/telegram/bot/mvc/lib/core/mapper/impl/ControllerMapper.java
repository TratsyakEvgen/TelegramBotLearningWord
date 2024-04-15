package com.tratsiak.telegram.bot.mvc.lib.core.mapper.impl;

import com.tratsiak.telegram.bot.mvc.lib.annotation.BotController;
import com.tratsiak.telegram.bot.mvc.lib.annotation.BotRequestMapping;
import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.lib.core.mapper.AbstractMapper;
import com.tratsiak.telegram.bot.mvc.lib.core.mapper.MapperException;
import com.tratsiak.telegram.bot.mvc.lib.core.mapper.ResponseException;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class ControllerMapper extends AbstractMapper {

    private final ApplicationContext context;


    @Autowired
    public ControllerMapper(ApplicationContext context) {
        methodsMap = new HashMap<>();
        this.context = context;
    }


    @Override
    protected BotView exceptionHandler(Exception e, Method method, Session session) throws MapperException {
        Throwable cause = e;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }

        if (cause instanceof ResponseException responseException) {
            BotView botView = responseException.getBotView();
            if (botView != null) {
                return botView;
            }
            String text = responseException.getMessage();
            return new BotView(SendMessage.builder().chatId(session.getId()).text(text).build());

        }
        throw new MapperException("Can't execute method " + method, e);
    }

    @Override
    protected void init() {
        Map<String, Object> mapControllerBeans = context.getBeansWithAnnotation(BotController.class);


        for (String bean : mapControllerBeans.keySet()) {
            BotRequestMapping annatationBotRequestMapping = context.findAnnotationOnBean(bean, BotRequestMapping.class);

            String path = "";
            if (annatationBotRequestMapping != null) {
                path = annatationBotRequestMapping.path();
            }

            Object object = mapControllerBeans.get(bean);

            for (Method method : object.getClass().getDeclaredMethods()) {
                BotRequestMapping annotationOnMethod = method.getDeclaredAnnotation(BotRequestMapping.class);


                if (annotationOnMethod != null) {
                    String finalPath = path + annotationOnMethod.path();
                    put(finalPath, method, object);
                }
            }
        }

    }


}
