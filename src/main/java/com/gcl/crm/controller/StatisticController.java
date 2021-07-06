package com.gcl.crm.controller;

import com.gcl.crm.entity.Potential;
import com.gcl.crm.form.RecentPotentialForm;
import com.gcl.crm.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/statistic")
public class StatisticController {
    private static final String STATISTIC_POTENTIAL_PAGE = "/statistic/statistic-potential-page";

    @Autowired
    StatisticService statisticService;

    @RequestMapping(value = "/potential", method = RequestMethod.GET)
    public String goStatisticPotential(Model model) {
        return STATISTIC_POTENTIAL_PAGE;
    }

    /*
    Return list 12 integer number of potentials present volatility
    URL: localhost:8082/statistic/potential/volatility?year=2021
     */
    @GetMapping(value = {"/potential/volatility"})
    @ResponseBody
    public List<Integer> getPotentialVolatility(@Nullable @RequestParam("year") Integer year){
        if (year == null){
            return null;
        }
        return statisticService.getPotentialVolatility(year);
    }

    /*
    Return list potentials of current date
    URL: localhost:8082/statistic/potential/today
     */
    @GetMapping(value = {"/potential/today"})
    @ResponseBody
    public List<RecentPotentialForm> getTodayPotential(){
        return statisticService.getTodayPotential();
    }
}
