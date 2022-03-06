package com.gcl.crm.service;

import com.gcl.crm.entity.TradingAccount;
import com.gcl.crm.form.TradingAccountSearchForm;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface TradingAccountService {
    List<TradingAccount> findTradingAccountByMonth();
    TradingAccount findTradingAccountByID(String id);
    List<TradingAccount> findAll();
    List<TradingAccount> findAccountStopDeal();
    TradingAccount  activateAccount(TradingAccount tradingAccount);
    void updateAccountBalance(String accountNumber,double balance);
    List<TradingAccount> findTradingAccountByMonthAndStatus(String month,String status);
    List<TradingAccount> findAllByNameNumberBalance(TradingAccountSearchForm tradingAccountSearchForm);
    TradingAccount createTradingAccount(TradingAccount tradingAccount);
    void exportDateInMonth(HttpServletResponse response) throws IOException;
}
