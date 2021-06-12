package com.gcl.crm.controller;

import com.gcl.crm.form.ComboboxForm;
import com.gcl.crm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String goHomePage(Model model) {
        return "/customer/home-customer-page-V2";
    }

    @RequestMapping(value = "/addCustomer", method = RequestMethod.GET)
    public String addCustomerPage(Model model) {
        return "/addCustomerPage.html";
    }

    @RequestMapping(value = "/initCombobox", method = RequestMethod.GET)
    public ResponseEntity initCombobox() {
        ComboboxForm comboboxForm = customerService.initComboboxData();
        return new ResponseEntity<>(comboboxForm, HttpStatus.OK);
    }
}
