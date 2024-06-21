package com.tratsiak.telegram.bot.mvc.lib.core.session.impl;

import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DefaultSession implements Session {

    private long id;
    private String textMessage;
    private String pastCommand;
    private String currentCommand;
    private String nextCommand;
    private Map<String, String> parameters;
    private Map<String, Object> entities;

    public DefaultSession(long id) {
        this.id = id;
        this.entities = new HashMap<>();
    }

    @Override
    public String getParam(String name) {
        return parameters.get(name);
    }
    @Override
    public Object getEntity(String name) {
        return entities.get(name);
    }
    @Override
    public void setEntity(String name, Object object) {
        entities.put(name, object);
    }



}
