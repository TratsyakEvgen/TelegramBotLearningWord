package com.tratsiak.telegram.bot.mvc.model.bean;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AuthTelegramApp {
    private String username;
    private String password;
    private long telegramId;
}
