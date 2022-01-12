package com.gcl.crm.controller;

import com.gcl.crm.entity.*;
import com.gcl.crm.enums.LevelEnum;
import com.gcl.crm.enums.Status;
import com.gcl.crm.form.CustomerSearchForm;
import com.gcl.crm.repository.ContractRepository;
import com.gcl.crm.service.*;
import com.gcl.crm.utils.WebUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.sql.Date;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/contract")
public class ContractController {

    private static final String VIEW_CUSTOMER_PAGE = "/contract/view-customer-page-v2";
    private static final String VIEW_TRADING_ACCOUNT_PAGE = "/contract/view-tradingAccount-page";
    private static final String OPEN_ACCOUNT_PAGE = "/contract/open-account-page";
    private static final String CREATE_CONTRACT_PAGE = "/contract/create-contract-page";
    private static final String  WAIT_CUSTOMER_PAGE="/contract/view-waiting-customer-page";

    @Autowired
    ContractService contractService;

    @Autowired
    CustomerProcessService customerProcessService;

    @Autowired
    ContractRepository contractRepository;

    @Autowired
    UserService userService;

    @Autowired
    TradingAccountService tradingAccountService;

    @Autowired
    ContractFileService contractFileService;

    @GetMapping("/viewContractCustomer")
    public String goContractPage(Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        model.addAttribute("userName", principal.getName());
        List<Contract> contracts = contractRepository.findAllByStatus("Đã Lưu HĐKH");
        model.addAttribute("contracts", contracts);
        model.addAttribute("userInfo", user);
        return "customer/view-customer-contract-page";
    }

    @GetMapping({"/manageCustomer"})
    public  String viewCustomer(Model model, Principal principal){
        User currentUser = userService.getUserByUsername(principal.getName());
        CustomerSearchForm customerSearchForm = new CustomerSearchForm();

        model.addAttribute("userInfo", currentUser);
        model.addAttribute("listCustomers",customerProcessService.getCustomerWaitingContract());
        model.addAttribute("searchForm",customerSearchForm);
        return VIEW_CUSTOMER_PAGE;
    }
    @GetMapping({"/waitingCustomer"})
    public  String getWaitingCustomer(Model model, Principal principal){
        User currentUser = userService.getUserByUsername(principal.getName());
        CustomerSearchForm customerSearchForm = new CustomerSearchForm();
        model.addAttribute("searchForm",customerSearchForm);
        model.addAttribute("userInfo", currentUser);
        model.addAttribute("listCustomers",customerProcessService.getWaitingCustomer());
        return WAIT_CUSTOMER_PAGE;
    }

    @RequestMapping(value = "/searchWaitingContract", method = RequestMethod.GET)
    public String searchWaitingContractCustomer(Model model, @Nullable @ModelAttribute("searchForm") CustomerSearchForm searchForm, Principal principal){
        if (searchForm == null){
            return "redirect:/contract/manageTradingAccount";
        }
        System.out.println(searchForm.getEmail());
        User currentUser = userService.getUserByUsername(principal.getName());
        model.addAttribute("userInfo", currentUser);
        model.addAttribute("listCustomers",customerProcessService.findWaitingContractCustomer(searchForm));
        return VIEW_CUSTOMER_PAGE;


    }
    @RequestMapping(value = "/searchWaitingCustomer", method = RequestMethod.GET)
    public String searchWaitingCustomer(Model model, @Nullable @ModelAttribute("searchForm") CustomerSearchForm searchForm, Principal principal){
        if (searchForm == null){
            return "redirect:/contract/manageTradingAccount";
        }
        List<Customer> customerList = customerProcessService.findWaitingCustomer(searchForm);
        User currentUser = userService.getUserByUsername(principal.getName());
        model.addAttribute("userInfo", currentUser);
        model.addAttribute("listCustomers",customerProcessService.findWaitingCustomer(searchForm));
        return WAIT_CUSTOMER_PAGE;

    }
    @GetMapping({"/manageOpenAccountPage"})
    public  String viewOpenAccountPage(Model model, Principal principal){
        User currentUser = userService.getUserByUsername(principal.getName());
        model.addAttribute("userInfo", currentUser);
        model.addAttribute("userName",principal.getName());
        model.addAttribute("listCustomers",customerProcessService.getAllCustomer());
        return "contract/view-open-account-page";
    }
    @GetMapping({"/manageTradingAccount"})
    public  String viewTradingAccount(Model model,Principal principal){
        User currentUser = userService.getUserByUsername(principal.getName());
        CustomerSearchForm customerForm = new CustomerSearchForm();
        model.addAttribute("customerForm",customerForm);
        model.addAttribute("userInfo", currentUser);

        model.addAttribute("listCustomers",customerProcessService.getCustomerHaveTradingAccount());

        return VIEW_TRADING_ACCOUNT_PAGE;
    }

    @RequestMapping(value = "/searchAllAccount", method = RequestMethod.GET)
    public String searchTradingAccount(Model model, @Nullable @ModelAttribute("customerForm") CustomerSearchForm searchForm, Principal principal){
        if (searchForm == null){
            return "redirect:/contract/manageTradingAccount";
        }
        if(searchForm.getStatus().equals("All")){
            CustomerSearchForm form = new CustomerSearchForm();
            if(searchForm.getAccountNumber()!=null){
                form.setAccountNumber(searchForm.getAccountNumber());
            }
            if(searchForm.getCustomerName()!=null){
                form.setCustomerName(searchForm.getCustomerName());
            }
            if(searchForm.getPhone() !=null){
                form.setPhone(searchForm.getPhone());
            }
            User currentUser = userService.getUserByUsername(principal.getName());
            model.addAttribute("listCustomers",customerProcessService.findCustomerByNumberPhoneStatus(form));

            model.addAttribute("userInfo", currentUser);
            return VIEW_TRADING_ACCOUNT_PAGE;
        }else {
            User currentUser = userService.getUserByUsername(principal.getName());
            model.addAttribute("listCustomers",customerProcessService.findCustomerByNumberPhoneStatus(searchForm));

            model.addAttribute("userInfo", currentUser);
            return VIEW_TRADING_ACCOUNT_PAGE;
        }

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
    public String createTradingAccount(@Valid @ModelAttribute("tradingAccount") TradingAccount tradingAccount,
                                       Model model,
                                       Principal principal,
                                       RedirectAttributes redirectAttributes)  {
        Customer customer =customerProcessService.findCustomerByID(tradingAccount.getCustomerID());
        User currentUser = userService.getUserByUsername(principal.getName());
        model.addAttribute("userInfo", currentUser);
        tradingAccount.setBrokerName(customer.getEmployee().getName());
        tradingAccount.setBrokerCode(customer.getEmployee().getId()+"");
        if(customer == null){
            return "redirect:/contract/waitingCustomer";

        }else{
            if(tradingAccount.getAccountNumber().length()<4){

                String url ="redirect:/contract/openAccount/"+customer.getCustomerId();
                redirectAttributes.addFlashAttribute("length","Mã tài khoản gồm 4 số ");
                model.addAttribute("tradingAccount",tradingAccount);
                return  url ;
            }else if(!tradingAccount.getAccountNumber().matches("[0-9]{4}")){
                String url ="redirect:/contract/openAccount/"+customer.getCustomerId();
                redirectAttributes.addFlashAttribute("number","Mã tài khoản là số  ");
                model.addAttribute("tradingAccount",tradingAccount);
                return  url ;
            }else if(tradingAccountService.findTradingAccountByID("003C"+tradingAccount.getBrokerCode()+tradingAccount.getAccountNumber()) != null){
                String url ="redirect:/contract/openAccount/"+customer.getCustomerId();
                redirectAttributes.addFlashAttribute("duplicate","Mã tài khoản này đã tồn tại");
                model.addAttribute("tradingAccount",tradingAccount);
                return  url ;
            }
            else{


                tradingAccount.setAccountNumber("003C"+tradingAccount.getBrokerCode()+tradingAccount.getAccountNumber());
                customer.setLevel(new Level(LevelEnum.LEVEL_6.getValue()));
                redirectAttributes.addFlashAttribute("flag","showAlert");

                customerProcessService.createTradingAccount(tradingAccount,customer);
                return "redirect:/contract/waitingCustomer";
            }

        }

    }
    @GetMapping({"/active/{id}"})
    public String activateAccount(@PathVariable(name="id") int id,RedirectAttributes redirectAttributes) throws ParseException {
        Customer customer = customerProcessService.findCustomerByID(id);
        customerProcessService.activateTradingAccount(customer);
        redirectAttributes.addFlashAttribute("flag","showAlert");

        return "redirect:/contract/manageTradingAccount";
    }
    @PostMapping({"/createContract"})
    public String createContract(RedirectAttributes redirectAttributes
            ,@ModelAttribute("contract") Contract contract
            ,@RequestParam("file") MultipartFile multipartFile) throws ParseException, IOException {
        Customer customer = customerProcessService.findCustomerByID(contract.getCustomer().getCustomerId());
        if(multipartFile == null){
            return "redirect:/contract/manageCustomer";

        }
        if(customer == null){
            return "redirect:/contract/manageCustomer";
        }else {
            contract.setId(contractService.getContractID());
            String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            ContractFile contractFile = new ContractFile();
            contractFile.setName(filename);
            contractFile.setContent(multipartFile.getBytes());
            contractFile.setSize(multipartFile.getSize());
            contractFile.setUploadTime(WebUtils.getSystemDate());
            contractFile.setActive(Status.ACTIVE);
            contract.setContractFile(contractFile);
            System.out.println("contract file : "+ contractFile.toString());
            customerProcessService.createContract(contract,customer);

            redirectAttributes.addFlashAttribute("flag","showAlert");

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
    public String updateTradingAccount(RedirectAttributes redirectAttributes,@ModelAttribute("tradingAccount") TradingAccount tradingAccount) throws ParseException {
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

            redirectAttributes.addFlashAttribute("flag","showAlert");

            customerProcessService.saveCustomer(customer);
            return "redirect:/contract/manageTradingAccount";
        }
    }


}

