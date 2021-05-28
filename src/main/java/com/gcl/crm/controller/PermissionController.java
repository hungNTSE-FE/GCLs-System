package com.gcl.crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/permission")
public class PermissionController {

    @RequestMapping(value = "/home",  method = RequestMethod.GET)
    public String getHome(Model model) {
        return "permission/home-permission-page";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String getEdit(Model model) { return "permission/edit-permission-page";}
}
