package com.tratsiak.telegram.bot.mvc.lib.core.session;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(SessionModifier.class)
public class DefaultSessionModifier implements SessionModifier {
    @Override
    public void modify(Session session) throws Exception {
    }
}
