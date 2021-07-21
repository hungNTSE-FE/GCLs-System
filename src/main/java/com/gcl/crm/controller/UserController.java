package com.gcl.crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    private static final String CHANGE_PASS_PAGE = "/profile-user/change-password";
    private static final String ACCOUNT_PROFILE_PAGE = "/profile-user/account-profile";

    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public String goChangePass(Model model) { return CHANGE_PASS_PAGE; }

    @RequestMapping(value = "/accountProfile", method = RequestMethod.GET)
    public String goAccountProfile(Model model) { return ACCOUNT_PROFILE_PAGE; }
}
