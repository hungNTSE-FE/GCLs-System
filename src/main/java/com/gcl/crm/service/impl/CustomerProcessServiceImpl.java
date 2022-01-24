package com.gcl.crm.service.impl;

import com.gcl.crm.entity.*;
import com.gcl.crm.form.CustomerSearchForm;
import com.gcl.crm.repository.CustomerProcessRepository;
import com.gcl.crm.service.ContractFileService;
import com.gcl.crm.service.ContractService;
import com.gcl.crm.service.CustomerProcessService;
import com.gcl.crm.service.TradingAccountService;
import com.gcl.crm.utils.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class CustomerProcessServiceImpl implements CustomerProcessService {

    @Autowired
    CustomerProcessRepository customerProcessRepository;

    @Autowired
    ContractService contractService;

    @Autowired
    ContractFileService contractFileService;

    @Autowired
    TradingAccountService tradingAccountService;

    @Override
    public List<Customer> getAllCustomer() {
        return customerProcessRepository.findAll();
    }

    @Override
    public Customer findCustomerByID(int id) {
        List<Customer> customerList = getAllCustomer();
        for(int i = 0 ; i < customerList.size();i++){
            if(customerList.get(i).getCustomerId().equals(id)){
                return customerList.get(i);
            }
        }
        return null;
    }

    @Override
    public void saveCustomer(Customer customer) {
        customerProcessRepository.save(customer);
    }

    @Override
    public List<Customer> getCustomerHaveTradingAccount() {
       return customerProcessRepository.getAllAccount("none");
    }

    @Override
    public void createTradingAccount(TradingAccount tradingAccount,Customer customer) {
        customer.setNumber(tradingAccount.getAccountNumber());
        customer.setCustomerCode(tradingAccount.getAccountNumber());
        tradingAccountService.createTradingAccount(tradingAccount);
        tradingAccount.setAccountNumber(customer.getNumber());
        tradingAccount.setAccountName(customer.getCustomerName());
        tradingAccount.setCustomer(customer);
        customer.setTradingAccount(tradingAccount);
        customerProcessRepository.save(customer);
    }

    @Override
    public void createContract(Contract contract, Customer customer) {
        contract.setCreateDate(Date.valueOf(LocalDate.now()));
        contract.setAccount_name(customer.getTradingAccount().getAccountName());
        contract.setBroker_name(customer.getTradingAccount().getBrokerName());
        contract.setBrokerCode(customer.getTradingAccount().getBrokerCode());
        contract.setNumber(customer.getTradingAccount().getAccountNumber());
        customer.setContractNumber(customer.getContractNumber());
        contract.setCustomer(customer);
        customer.setContract(contract);

        customerProcessRepository.save(customer);
    }

    @Override
    public void activateTradingAccount(Customer customer) {
        TradingAccount tradingAccount = customer.getTradingAccount();
        tradingAccountService.activateAccount(tradingAccount);
        tradingAccount.setCustomer(customer);
        customer.setTradingAccount(tradingAccount);
        customerProcessRepository.save(customer);
    }

    @Override
    public List<Customer> getAllContractCustomer() {
        return customerProcessRepository.getAllPotentialCustomer("GCL");
    }

    @Override
    public void saveAvatar(MultipartFile multipartFile, Customer customer) {
        String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String uploadDir = "customerIdentification/"+customer.getCustomerId();
        try {
            FileUploadUtils.saveFile(uploadDir, filename, multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Customer> getWaitingCustomer() {
        return customerProcessRepository.getWaitingCustomer("none");
    }

    @Override
    public List<Customer> getCustomerWaitingContract() {
        return  customerProcessRepository.getCustomerNotContract("none","none");
    }

    @Override
    public List<Customer> findCustomerByNumberPhoneStatus(CustomerSearchForm customerSearchForm) {
        return customerProcessRepository.searchAccountByNumberNamePhoneStatus(customerSearchForm.getStatus(),customerSearchForm.getPhone(),customerSearchForm.getAccountNumber(),customerSearchForm.getCustomerName());
    }

    @Override
    public List<Customer> findWaitingCustomer(CustomerSearchForm customerSearchForm) {
        return customerProcessRepository.searchWaitingCustomer(customerSearchForm.getCustomerName(),customerSearchForm.getPhone(),customerSearchForm.getEmail(),"none");
    }

    @Override
    public List<Customer> findWaitingContractCustomer(CustomerSearchForm customerForm) {
        return customerProcessRepository.searchCustomerNotContract(customerForm.getCustomerName()
                ,customerForm.getEmail(),customerForm.getPhone(),
                customerForm.getAccountNumber(),"none","none");
    }

    @Override
    public void downloadContractFile(ContractFile contractFile,HttpServletResponse response) throws IOException {
        contractFileService.download(contractFile,response);
    }


}
