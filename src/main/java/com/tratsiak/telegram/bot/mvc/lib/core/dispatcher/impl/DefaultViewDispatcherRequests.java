package com.tratsiak.telegram.bot.mvc.lib.core.dispatcher.impl;

import com.tratsiak.telegram.bot.mvc.lib.annotation.BotStaticResource;
import com.tratsiak.telegram.bot.mvc.lib.annotation.BotViewStaticResource;
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
public class DefaultViewDispatcherRequests extends AbstractDispatcherRequests {
    @Autowired
    public DefaultViewDispatcherRequests(PathValidator pathValidator) {
        super(pathValidator);
    }

    @Override
    public void init(ApplicationContext context) {
        Map<String, Object> mapControllerBeans = context.getBeansWithAnnotation(BotViewStaticResource.class);

        for (String bean : mapControllerBeans.keySet()) {
            BotStaticResource annotationOnBean = context.findAnnotationOnBean(bean, BotStaticResource.class);

            String path = "";
            if (annotationOnBean != null) {
                path = annotationOnBean.path();
            }

            Object object = mapControllerBeans.get(bean);

            for (Method method : object.getClass().getDeclaredMethods()) {
                BotStaticResource annotationOnMethod = method.getDeclaredAnnotation(BotStaticResource.class);

                if (annotationOnMethod != null) {
                    String finalPath = path + annotationOnMethod.path();
                    put(finalPath, method, object);

                }
            }
        }
    }

}
