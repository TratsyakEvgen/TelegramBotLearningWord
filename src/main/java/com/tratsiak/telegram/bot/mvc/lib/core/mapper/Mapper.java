package com.tratsiak.telegram.bot.mvc.lib.core.mapper;

import com.tratsiak.telegram.bot.mvc.lib.core.BotView;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;

public interface Mapper {

    BotView executeMethod(String path, Session session) throws MapperException;
}
