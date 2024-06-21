package com.tratsiak.telegram.bot.mvc.lib.core.dispatcher.impl;

import com.tratsiak.telegram.bot.mvc.lib.annotation.BotController;
import com.tratsiak.telegram.bot.mvc.lib.annotation.BotRequestMapping;
import com.tratsiak.telegram.bot.mvc.lib.core.dispatcher.AbstractDispatcherRequests;
import com.tratsiak.telegram.bot.mvc.lib.core.path.PathValidator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

@Component
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class DefaultControllerDispatcherRequests extends AbstractDispatcherRequests {

    @Autowired
    public DefaultControllerDispatcherRequests(PathValidator pathValidator, ApplicationContext context) {
        super(pathValidator, context);
    }

    @Override
    public void init() {
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
