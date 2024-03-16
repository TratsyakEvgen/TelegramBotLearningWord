package com.tratsiak.telegram.bot.mvc.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Token {
    private String access;
    private String refresh;
}
