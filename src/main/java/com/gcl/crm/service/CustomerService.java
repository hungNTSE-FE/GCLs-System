package com.gcl.crm.service;

import com.gcl.crm.dto.CustomerDTO;
import com.gcl.crm.dto.ErrorInFo;
import com.gcl.crm.entity.BankAccount;
import com.gcl.crm.entity.User;
import com.gcl.crm.form.ComboboxForm;
import com.gcl.crm.form.CustomerForm;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

public interface CustomerService {
    ComboboxForm initComboboxData();
    CustomerForm initForm(Long potentialId);
    BankAccount registerBanking(CustomerForm customerForm) throws ParseException;
    List<ErrorInFo> checkBussinessBeforeRegistCustomer(CustomerForm customerForm);
    void registerCustomer(CustomerForm customerForm, User user, MultipartFile before, MultipartFile after);
    boolean importCustomer(List<CustomerDTO> customers, User user);
}
