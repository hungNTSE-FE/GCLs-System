package com.gcl.crm.controller;

import com.gcl.crm.form.CustomerStatusForm;
import com.gcl.crm.service.MarketingServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @RequestMapping(value = "/marketing/getCustomerStatusReport")
    public String getCustomerStatusReport() {
        List<CustomerStatusForm> customerStatusForm = maketingServices.getListCustomerStatusReport("2021/01/01", "2021/06/25");
        return CUSTOMER_REPORT_PAGE;
    }


}
