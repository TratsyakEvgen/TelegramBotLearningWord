package com.tratsiak.telegram.bot.mvc.lib.core.session;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Session {

    private long id;
    private String text;
    private String pastCommand;
    private String currentCommand;
    private String nextCommand;
    private Map<String, String> parameters;
    private Map<String, Object> entities = new HashMap<>();

    public Session(long id) {
        this.id = id;
    }

    public void clearTemporary() {
        pastCommand = currentCommand;
        text = null;
        currentCommand = null;
        parameters = null;
    }

    public String getParam(String name) {
        return parameters.get(name);
    }
    public Object getEntity(String name) {
        return entities.get(name);
    }

    public void setEntity(String name, Object object) {
        entities.put(name, object);
    }



}
