package com.tratsiak.telegram.bot.mvc.lib.core.dispatcher;


import java.util.Map;

public interface DispatchersRequestsInitializer {
    void init();

    Map<String, DispatcherRequests> getDispatcherRequestsMap();
}
