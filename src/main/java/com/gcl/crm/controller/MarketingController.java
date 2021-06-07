package com.gcl.crm.controller;

import com.gcl.crm.service.MarketingServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MarketingController {

    public static final String MAIN_PAGE = "marketing.html";

    @Autowired
    MarketingServices maketingServices;

    @RequestMapping(value = "/marketing")
    public String initScreen(Model model) {
        maketingServices.initScreen();
        return MAIN_PAGE;
    }


}
