package com.tratsiak.telegram.bot.mvc.lib.core.mapper.impl;

import com.tratsiak.telegram.bot.mvc.lib.annotation.BotStaticResource;
import com.tratsiak.telegram.bot.mvc.lib.annotation.BotViewStaticResource;
import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.lib.core.mapper.AbstractMethodMapper;
import com.tratsiak.telegram.bot.mvc.lib.core.mapper.MethodMapperException;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

@Component
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class ViewMethodMapper extends AbstractMethodMapper {
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

    @Override
    protected BotView exceptionHandler(Exception e, Method method, Session session) throws MethodMapperException {
        throw new MethodMapperException("Can't execute method " + method, e);
    }


}
