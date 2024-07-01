package com.tratsiak.telegram.bot.learning.word.view.util;


import com.tratsiak.telegram.bot.learning.word.model.Page;
import com.tratsiak.telegrambotmvc.components.ComponentInlineKeyboardButton;
import com.tratsiak.telegrambotmvc.components.ComponentInlineKeyboardMarkup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class PageUtil {
    private final ComponentInlineKeyboardButton compInlineBtn;
    private final ComponentInlineKeyboardMarkup compInlineMarkup;

    @Autowired
    public PageUtil(ComponentInlineKeyboardButton compInlineBtn, ComponentInlineKeyboardMarkup compInlineMarkup) {
        this.compInlineBtn = compInlineBtn;
        this.compInlineMarkup = compInlineMarkup;
    }

    public void getNavbar(InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder, Page<?> page, String path) {

        int totalPage = page.getTotalPages();
        int currentPage = page.getNumber();

        List<InlineKeyboardButton> navbar = null;

        if (totalPage > 1) {
            navbar = new ArrayList<>();
        }

        if (currentPage > 0) {
            assert navbar != null;
            navbar.add(compInlineBtn.get("<<", path + "page=0"));
            navbar.add(compInlineBtn.get("<", path + "page=" + (currentPage - 1)));
        }

        if (totalPage != 0 && currentPage < totalPage - 1) {
            assert navbar != null;
            navbar.add(compInlineBtn.get(">", path + "page=" + (currentPage + 1)));
            navbar.add(compInlineBtn.get(">>", path + "page=" + (totalPage - 1)));
        }

        if (navbar != null) {
            compInlineMarkup.row(builder, navbar);
        }
    }
}
