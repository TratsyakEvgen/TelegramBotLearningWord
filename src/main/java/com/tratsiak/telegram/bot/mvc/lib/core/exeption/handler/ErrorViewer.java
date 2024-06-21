package com.tratsiak.telegram.bot.mvc.lib.core.exeption.handler;

import com.tratsiak.telegram.bot.mvc.lib.core.View;
import com.tratsiak.telegram.bot.mvc.lib.core.session.Session;

public interface ErrorViewer {

    View getDefaultError(Session session);

}
