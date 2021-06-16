package com.gcl.crm.controller;

import com.gcl.crm.form.CampaignDetailForm;
import com.gcl.crm.form.CustomerStatusForm;
import com.gcl.crm.service.MarketingServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MarketingController {

    public static final String MAIN_PAGE = "marketing/marketing.html";
    public static final String CUSTOMER_REPORT_PAGE = "marketing/customerStatusReport.html";

    @Autowired
    MarketingServices maketingServices;

    @RequestMapping(value = "/marketing")
    public String initScreen(Model model) {
        maketingServices.initScreen();
        return MAIN_PAGE;
    }

    @RequestMapping(value = "/marketing/customerStatusReport")
    public String initScreenCustomerStatusReport() {
        return CUSTOMER_REPORT_PAGE;
    }

    @GetMapping(value = "/marketing/getListCustomerStatusReport")
    @ResponseBody
    public ResponseEntity<List<CustomerStatusForm>> getCustomerStatusReport(@RequestParam String fromDate,
                                                                            @RequestParam String toDate) {
        List<CustomerStatusForm> customerStatusForm = maketingServices.
                                                            getListCustomerStatusReport(fromDate, toDate);
        return new ResponseEntity<>(customerStatusForm, HttpStatus.OK);
    }
}
