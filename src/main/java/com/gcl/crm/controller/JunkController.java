package com.gcl.crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/junk")
public class JunkController {

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String goHomePage(Model model) {
        return "/junk/data-junk-page-hungNT";
    }
}
