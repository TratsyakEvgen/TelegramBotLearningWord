package com.tratsiak.telegram.bot.mvc.lib.core.mapper;

import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import org.springframework.context.ApplicationContext;

public interface MethodMapper {

    BotView executeMethod(String path, Session session) throws MethodMapperException;
    void init(ApplicationContext context);
}
