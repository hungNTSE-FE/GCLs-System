package com.gcl.crm.controller;

import com.gcl.crm.entity.User;
import com.gcl.crm.form.CustomerStatusForm;
import com.gcl.crm.form.CustomerStatusReportForm;
import com.gcl.crm.form.KPIMktGroupForm;
import com.gcl.crm.form.SourceEvaluationForm;
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
    private static final String SOURCE_PAGE = "/report/source-page";
    private static final String AGENCY_PAGE = "/report/agency-page";
    private static final String CUSTOMER_STATUS_PAGE = "/report/customerStatus-page";
    private static final String SUMMARIZE_PAGE = "/report/summary-page";

    @Autowired
    UserService userService;

    @RequestMapping(value = "/source", method = RequestMethod.GET)
    public String goReportSource(Model model, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        model.addAttribute("userName", principal.getName());
        model.addAttribute("userInfo", currentUser);
        model.addAttribute("sourceEvaluationForm", new SourceEvaluationForm());
        return SOURCE_PAGE;
    }

    @RequestMapping(value = "/agency", method = RequestMethod.GET)
    public String goReportAgency(Model model, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        model.addAttribute("userName", principal.getName());
        model.addAttribute("userInfo", currentUser);
        model.addAttribute("KPI_MKT_GROUP_FORM" , new KPIMktGroupForm());
        return AGENCY_PAGE;
    }

    @RequestMapping(value = "/customerStatus", method = RequestMethod.GET)
    public String goReportCustomerStatusPage(Model model, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        model.addAttribute("userName", principal.getName());
        model.addAttribute("userInfo", currentUser);
        model.addAttribute("customerStatusReportForm", new CustomerStatusReportForm());
        return CUSTOMER_STATUS_PAGE;
    }

    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    public String goReportSummaryPage(Model model, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        model.addAttribute("userName", principal.getName());
        model.addAttribute("userInfo", currentUser);
        return SUMMARIZE_PAGE;
    }
}
