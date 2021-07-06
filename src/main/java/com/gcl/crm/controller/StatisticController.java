package com.gcl.crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/statistic")
public class StatisticController {
    private static final String STATISTIC_POTENTIAL_PAGE = "/statistic/statistic-potential-page";

    @RequestMapping(value = "/potential", method = RequestMethod.GET)
    public String goStatisticPotential(Model model) { return STATISTIC_POTENTIAL_PAGE; }
}
