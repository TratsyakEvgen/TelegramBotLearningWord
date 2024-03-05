package com.tratsiak.telegram.bot.mvc.controller;

import com.tratsiak.telegram.bot.mvc.lib.controller.BotController;
import com.tratsiak.telegram.bot.mvc.lib.controller.BotRequestMapping;
import com.tratsiak.telegram.bot.mvc.lib.view.BotView;
import com.tratsiak.telegram.bot.mvc.view.View;

@BotController
public class Controller {

    @BotRequestMapping(path = "/start")
    private BotView hello(){
        return View.start();
    }

}
