package com.tratsiak.telegram.bot.mvc.lib.core.mapper.impl;

import com.tratsiak.telegram.bot.mvc.lib.annotation.BotController;
import com.tratsiak.telegram.bot.mvc.lib.annotation.BotRequestMapping;
import com.tratsiak.telegram.bot.mvc.lib.core.mapper.AbstractMapper;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

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
