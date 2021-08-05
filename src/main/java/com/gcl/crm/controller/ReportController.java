package com.gcl.crm.controller;

import com.gcl.crm.entity.User;
import com.gcl.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
@RequestMapping("/report")
public class ReportController {
    private final String SOURCE_PAGE = "/report/source-page";

    @Autowired
    UserService userService;

    @RequestMapping(value = "/source", method = RequestMethod.GET)
    public String goReportSource(Model model, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        model.addAttribute("userName", principal.getName());
        model.addAttribute("userInfo", currentUser);
        return SOURCE_PAGE;
    }
}
