package com.gcl.crm.controller;

import com.gcl.crm.entity.Potential;
import com.gcl.crm.form.ComboboxForm;
import com.gcl.crm.form.CustomerForm;
import com.gcl.crm.service.CustomerService;
import com.gcl.crm.service.PotentialService;
import com.gcl.crm.utils.ExcelReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private static final String ADD_CUSTOMER_PAGE = "customer/create-customer-page-V2";
    private static final String CUSTOMER_FORM = "CustomerForm";

    @Autowired
    CustomerService customerService;

    @Autowired
    PotentialService potentialService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String goHomePage(Model model) {
        return "/customer/home-customer-page-V2";
    }

    @RequestMapping(value = "/id1", method = RequestMethod.GET)
    public String goDetailCustomer(Model model) {
        return "/customer/details/detail-customer-overview-page-V2";
    }

    @RequestMapping(value = "/information/id1", method = RequestMethod.GET)
    public String goDetailInformationCustomer(Model model) {
        return "/customer/details/detail-customer-information-page-V2";
    }

    @RequestMapping(value = "/email/id1", method = RequestMethod.GET)
    public String goDetailEmailCustomer(Model model) {
        return "/customer/details/detail-customer-email-page-V2";
    }

    @RequestMapping(value = "/action/id1", method = RequestMethod.GET)
    public String goDetailActionCustomer(Model model) {
        return "/customer/details/detail-customer-action-page-V2";
    }

    @RequestMapping(value = "/addCustomer", method = RequestMethod.GET)
    public String addCustomerPage(Model model, @ModelAttribute("CustomerForm") CustomerForm customerForm) {
        ComboboxForm comboboxForm = customerService.initComboboxData();
        customerForm.setComboboxForm(comboboxForm);
        model.addAttribute(CUSTOMER_FORM, customerForm);
        return ADD_CUSTOMER_PAGE;
    }

    @PostMapping(value = "/registerCustomer")
    public String saveCustomer(Model model, @Valid @ModelAttribute(CUSTOMER_FORM) CustomerForm customerForm
            , BindingResult result, Errors errors) {
        customerService.registerCustomer(customerForm);
        ComboboxForm comboboxForm = customerService.initComboboxData();
        customerForm.setComboboxForm(comboboxForm);
        model.addAttribute(CUSTOMER_FORM, customerForm);
        return ADD_CUSTOMER_PAGE;
    }

    @PostMapping("/import")
    public String importExcelFile(Model model, @RequestParam("upload") MultipartFile file){
        ExcelReader excelReader = new ExcelReader();
        try {
            List<Potential> potentialData = excelReader.getPotentialData(file.getInputStream(), file.getOriginalFilename());
            potentialService.importPotential(potentialData);
            model.addAttribute("message", "Dữ liệu mới đã được lưu vào hệ thống");
        } catch (IOException e) {
            model.addAttribute("error", "Không thể mở tệp đã chọn");
        }
        return "redirect:/customer/home";
    }
}
