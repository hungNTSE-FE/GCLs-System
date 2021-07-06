package com.gcl.crm.controller;

import com.gcl.crm.entity.*;
import com.gcl.crm.service.*;
import com.gcl.crm.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("/contract")
public class ContractController {
//    @GetMapping({"/home"})
//    public  String viewTransactionPage(){
//        return "transaction/home-transaction-page";
//    }
    @Autowired
    ContractService contractService;

    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerProcessService customerProcessService;


    @Autowired
    private TransactionService transactionService;
    @GetMapping({"/manageCustomer"})
    public  String viewCustomer(Model model){
        model.addAttribute("listCustomers",customerProcessService.getAllCustomer());

        return "contract/view-customer-page";
    }

    @GetMapping({"/openAccount/{id}"})
    public String showContractCreatePage(@PathVariable(name="id") String id ,Model model){
        Customer customer = customerProcessService.findCustomerByID(id);
        model.addAttribute("customer",customer);
        TradingAccount tradingAccount = new TradingAccount();
        model.addAttribute("tradingAccount",tradingAccount);
        List<BankAccount> bankAccountList = customer.getBankAccounts();
        System.out.println(bankAccountList.size());
        model.addAttribute("owner",bankAccountList.get(0).getOwnerName());
        model.addAttribute("bankAccount",bankAccountList);
        return "contract/open-account-page";
    }

    @PostMapping({"/createAccount"})
    public String createContract(@RequestParam(name="customerCode") String customerCode,@ModelAttribute("tradingAccount") TradingAccount tradingAccount) throws ParseException {



        Customer customer =customerProcessService.findCustomerByID(customerCode);
        customer.setNumber(tradingAccount.getAccountNumber());
        tradingAccount.setCreateDate(WebUtils.getSystemDate());
        tradingAccount.setCustomer(customer);
        customer.setTradingAccount(tradingAccount);


        customerProcessService.saveCustomer(customer);
        return "redirect:/contract/manageCustomer";
    }

}
