package com.tratsiak.telegram.bot.mvc.lib.core.mapper;


import java.util.Map;

public interface MethodMapperInitializer {
    void init();
     Map<String, MethodMapper> getMethodMappers();
}
