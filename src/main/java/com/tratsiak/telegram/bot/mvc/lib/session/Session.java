package com.tratsiak.telegram.bot.mvc.lib.session;

import lombok.*;

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
    private Map<String,String> parameters;
    private Map<String, Object> entities;

    public Session(long id) {
        this.id = id;
    }


}
