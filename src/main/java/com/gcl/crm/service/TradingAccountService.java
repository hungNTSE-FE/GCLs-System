package com.gcl.crm.service;

import com.gcl.crm.entity.TradingAccount;

import java.util.List;

public interface TradingAccountService {
    List<TradingAccount> findTradingAccountByMonth(String month);
    List<TradingAccount> findAll();
    List<TradingAccount> findAccountStopDeal();
    void updateAccountBalance(String accountNumber,double balance);
    List<TradingAccount> findTradingAccountByMonthAndStatus(String month,String status);
}
