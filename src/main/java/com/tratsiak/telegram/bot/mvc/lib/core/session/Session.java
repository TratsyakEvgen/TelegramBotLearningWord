package com.tratsiak.telegram.bot.mvc.lib.core.session;

import java.util.Map;


public interface Session {


    long getId();

    void setId(long id);

    String getPastCommand();

    void setPastCommand(String pastCommand);

    String getCurrentCommand();

    void setCurrentCommand(String currentCommand);

    String getNextCommand();

    void setNextCommand(String nextCommand);

    Map<String, String> getParameters();

    String getParam(String name);

    void setParameters(Map<String, String> parameters);

    Map<String, Object> getEntities();

    Object getEntity(String name);

    void setEntity(String name, Object object);
    void setEntities(Map<String, Object> entities);

    String getTextMessage();
    void setTextMessage(String textMessage);
}
