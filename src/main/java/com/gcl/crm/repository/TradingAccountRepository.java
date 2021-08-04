package com.gcl.crm.repository;

import com.gcl.crm.entity.TradingAccount;
import com.gcl.crm.form.TradingAccountForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TradingAccountRepository  extends JpaRepository<TradingAccount,String> {
    TradingAccount getTradingAccountByAccountNumber(String accountNumber);
}
