package com.gcl.crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {
    private static final String LOGIN_PAGE = "loginPage";
    private static final String FORGOT_PAGE = "forgot-password";
    private static final String PAGE_ERROR_403 = "error/error-403";

    @RequestMapping(value = {"/welcome", "/"}, method = RequestMethod.GET)
    public String welcomePage(Model model) {
        model.addAttribute("title", "Welcome");
        model.addAttribute("message", "This is welcome page!");
        return "redirect:/department/home";
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String getLoginPage(Model model) {
        return LOGIN_PAGE;
    }

    @RequestMapping(value = "/forgot-password", method = RequestMethod.GET)
    public String getForgotPassword(Model model) { return FORGOT_PAGE; }

    @GetMapping(value = {"/403"})
    public String accessDenied(){
        return PAGE_ERROR_403;
    }
}
