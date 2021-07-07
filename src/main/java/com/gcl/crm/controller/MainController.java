package com.gcl.crm.controller;

import com.gcl.crm.utils.WebUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class MainController {
    private static final String LOGIN_PAGE = "loginPage";

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String welcomePage(Model model) {
        model.addAttribute("title", "Welcome");
        model.addAttribute("message", "This is welcome page!");
        return "redirect:/department/home";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String DefaultPage(Model model) {
        return LOGIN_PAGE;
    }
}
