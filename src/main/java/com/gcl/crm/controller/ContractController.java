package com.gcl.crm.controller;

import com.gcl.crm.entity.*;
import com.gcl.crm.service.*;
import com.gcl.crm.utils.WebUtils;

import org.apache.poi.ss.usermodel.PrintCellComments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Date;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/contract")
public class ContractController {

    @Autowired
    ContractService contractService;

    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerProcessService customerProcessService;
    @Autowired
    UserService userService;
    private static final String VIEW_CUSTOMER_PAGE = "/contract/view-customer-page";
    private static final String VIEW_TRADING_ACCOUNT_PAGE = "/contract/view-tradingAccount-page";
    private static final String OPEN_ACCOUNT_PAGE = "/contract/open-account-page";
    private static final String CREATE_CONTRACT_PAGE = "/contract/create-contract-page";

    @Autowired
    private TransactionService transactionService;
    @GetMapping({"/manageCustomer"})
    public  String viewCustomer(Model model, Principal principal){
        User currentUser = userService.getUserByUsername(principal.getName());
        model.addAttribute("userInfo", currentUser);
        model.addAttribute("listCustomers",customerProcessService.getAllCustomer());
        return VIEW_CUSTOMER_PAGE;
    }
    @GetMapping({"/manageTradingAccount"})
    public  String viewTradingAccount(Model model,Principal principal){
        User currentUser = userService.getUserByUsername(principal.getName());
        model.addAttribute("userInfo", currentUser);
        model.addAttribute("listCustomers",customerProcessService.getCustomerHaveTradingAccount());

        return VIEW_TRADING_ACCOUNT_PAGE;
    }


    @GetMapping({"/openAccount/{id}"})
    public String showOpenAccountPage(@PathVariable(name="id") int id ,Model model,Principal principal) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Customer customer = customerProcessService.findCustomerByID(id);
        model.addAttribute("customer",customer);
        if(customer == null){
            return "redirect:/contract/manageCustomer";
        }else{
            customer.setBirthDate(format.format(customer.getIdentification().getBirthDate()));
            customer.setIssueDate(format.format(customer.getIdentification().getIssueDate()));

            TradingAccount tradingAccount = new TradingAccount();
            tradingAccount.setCustomerID(id);
            model.addAttribute("tradingAccount",tradingAccount);
            List<BankAccount> bankAccountList = customer.getBankAccounts();
            User currentUser = userService.getUserByUsername(principal.getName());
            model.addAttribute("userInfo", currentUser);
            model.addAttribute("owner",bankAccountList.get(0).getOwnerName());
            model.addAttribute("bankAccount",bankAccountList);
            return OPEN_ACCOUNT_PAGE;
        }

    }
    @GetMapping({"/showCreateContract/{id}"})
    public String showContractCreatePage(@PathVariable(name="id") int id , Model model, Principal principal){
        Customer customer = customerProcessService.findCustomerByID(id);
        if(customer == null){
            return "redirect:/contract/manageCustomer";
        }else{
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            customer.setBirthDate(format.format(customer.getIdentification().getBirthDate()));
            customer.setIssueDate(format.format(customer.getIdentification().getIssueDate()));
            String strDate = customer.getCreateDate()+"";
            String[] createDate = strDate.split(" ");
            model.addAttribute("createDate",createDate[0]);
            Contract contract = new Contract();
            contract.setCustomer(customer);
            contract.setId(contractService.getContractID());
            User currentUser = userService.getUserByUsername(principal.getName());
            model.addAttribute("userInfo", currentUser);
            model.addAttribute("contract",contract);
            model.addAttribute("customer",customer);
            model.addAttribute("customerID",customer.getCustomerId());
        }


        return CREATE_CONTRACT_PAGE;
    }

    @PostMapping({"/createAccount"})
    public String createTradingAccount(@ModelAttribute("tradingAccount") TradingAccount tradingAccount,Model model,Principal principal)  {
        Customer customer =customerProcessService.findCustomerByID(tradingAccount.getCustomerID());
        User currentUser = userService.getUserByUsername(principal.getName());
        model.addAttribute("userInfo", currentUser);
        if(customer == null){
            return "redirect:/contract/manageTradingAccount";

        }else{
            customerProcessService.createTradingAccount(tradingAccount,customer);
            return "redirect:/contract/manageCustomer";
        }

    }
    @GetMapping({"/active/{id}"})
    public String activateAccount(@PathVariable(name="id") int id) throws ParseException {
        Customer customer = customerProcessService.findCustomerByID(id);
        customerProcessService.activateTradingAccount(customer);
        return "redirect:/contract/manageTradingAccount";
    }
    @PostMapping({"/createContract"})
    public String createContract(@ModelAttribute("contract") Contract contract) throws ParseException {
        Customer customer = customerProcessService.findCustomerByID(contract.getCustomer().getCustomerId());

        if(customer == null){
            return "redirect:/contract/manageCustomer";
        }else {
            customerProcessService.createContract(contract,customer);
            return "redirect:/contract/manageCustomer";

        }

    }

    @GetMapping({"/showUpdateAccountForm/{id}"})
    public String showUpdateAccountForm(@PathVariable(name="id") int id ,Model model,Principal principal){
        Customer customer = customerProcessService.findCustomerByID(id);
        User currentUser = userService.getUserByUsername(principal.getName());
        model.addAttribute("userInfo", currentUser);
        if(customer == null){
            return "redirect:/contract/manageTradingAccount";
        }else {
            model.addAttribute("customer",customer);
            model.addAttribute("tradingAccount",customer.getTradingAccount());
            return "contract/update-account-page";
        }

    }

    @PostMapping({"/updateAccount"})
    public String updateTradingAccount(@ModelAttribute("tradingAccount") TradingAccount tradingAccount) throws ParseException {
        Customer customer =customerProcessService.findCustomerByID(tradingAccount.getCustomerID());
        if(customer == null){
           return "redirect:/contract/manageTradingAccount";
        }else {
            customer.setNumber(customer.getCustomerCode());
            tradingAccount.setAccountName(customer.getCustomerName());
            tradingAccount.setCreateDate(customer.getTradingAccount().getCreateDate());
            tradingAccount.setUpdateDate(Date.valueOf(LocalDate.now()));
            tradingAccount.setUpdateType(tradingAccount.getStatus());

            tradingAccount.setCustomer(customer);
            customer.setTradingAccount(tradingAccount);


            customerProcessService.saveCustomer(customer);
            return "redirect:/contract/manageTradingAccount";
        }

    }

}
