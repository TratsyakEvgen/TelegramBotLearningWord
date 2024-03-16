package com.tratsiak.telegram.bot.mvc.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;
import com.tratsiak.telegram.bot.mvc.lib.core.session.SessionModifier;
import com.tratsiak.telegram.bot.mvc.model.AuthTelegramApp;
import com.tratsiak.telegram.bot.mvc.model.Token;
import com.tratsiak.telegram.bot.mvc.repository.RepositoryException;
import com.tratsiak.telegram.bot.mvc.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider implements SessionModifier {

    private final static String JWT_NAME = "jwt";

    private final TokenRepository tokenRepository;
    private final String username;
    private final String password;


    @Autowired
    public JwtProvider(TokenRepository tokenRepository,
                       @Value("${telegram.username}") String username,
                       @Value("${telegram.password}") String password) {
        this.tokenRepository = tokenRepository;
        this.username = username;
        this.password = password;
    }

    @Override
    public void modify(Session session) throws JwtProviderException {
        Token token = (Token) session.getEntity(JWT_NAME);
        Date date = new Date(System.currentTimeMillis() + 10 * 1000);


        if (token == null || isExpires(token.getRefresh(), date)) {
            AuthTelegramApp authTelegramApp = AuthTelegramApp.builder()
                    .username(username)
                    .password(password)
                    .telegramId(session.getId())
                    .build();
            try {
                token = tokenRepository.getNewToken(authTelegramApp);
            } catch (RepositoryException e) {
                throw new JwtProviderException("Can't get token", e);
            }

            session.setEntity(JWT_NAME, token);
            return;
        }

        if (isExpires(token.getAccess(), date)){
            try {
                token = tokenRepository.updateToken(token);
                session.setEntity(JWT_NAME, token);
            } catch (RepositoryException e) {
                throw new JwtProviderException("Can't update token", e);
            }
        }
    }

    private boolean isExpires(String token, Date date) throws JwtProviderException {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            Date expires = decodedJWT.getExpiresAt();
            return expires.before(date);
        } catch (JWTDecodeException e) {
            throw new JwtProviderException("Can't parse token", e);
        }
    }



}
