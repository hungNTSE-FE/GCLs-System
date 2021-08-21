package com.gcl.crm.service;

import com.gcl.crm.entity.*;
import com.gcl.crm.enums.LevelEnum;
import com.gcl.crm.enums.Status;
import com.gcl.crm.repository.CustomerRepository;
import com.gcl.crm.repository.PotentialRepository2;
import com.gcl.crm.repository.TradingAccountRepository;
import com.gcl.crm.repository.TransactionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TransactionHistoryServiceImpl implements TransactionHistoryService{
    @Autowired
    TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    TradingAccountRepository tradingAccountRepository;
    @Override
    public List<TransactionHistory> getAllTransactionHistory() {
        return transactionHistoryRepository.findAll();
    }

    @Autowired
    PotentialRepository2 potentialRepository2;

    @Autowired
    CustomerRepository customerRepository;

    @Override
    @Transactional
    public boolean importTransactionHistory(List<TransactionHistory> transactionHistories) {
        if (transactionHistories.size() == 0){
            return false;
        }
        transactionHistories = transactionHistories
                                        .stream()
                                        .filter(transactionHistory -> Objects.isNull(transactionHistoryRepository
                                                                        .getTransactionHistoryByTransactionID(
                                                                                transactionHistory.getTransactionID())))
                                        .map(transactionHistory -> {
                                            TradingAccount tradingAccount = tradingAccountRepository
                                                    .getTradingAccountByAccountNumber(transactionHistory.getTradingAccount()
                                                                                                        .getAccountNumber());
                                            if (Objects.nonNull(tradingAccount)) {
                                                transactionHistory.setTradingAccount(tradingAccount);
                                                return transactionHistory;
                                            }
                                            return null;
                                        })
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(transactionHistories)) {
            transactionHistoryRepository.saveAll(transactionHistories);
        }

        List<String> accountnumberDistinct = transactionHistories.stream()
                .map(x -> x.getTradingAccount().getAccountNumber())
                .collect(Collectors.toList())
                .stream()
                .distinct()
                .collect(Collectors.toList());
        accountnumberDistinct.forEach(accountNumber -> {
            potentialRepository2.updateLevelPotentialByAccountnumber(accountNumber, LevelEnum.LEVEL_7.getValue());
            customerRepository.updateCustomerByAccountNumber(accountNumber, LevelEnum.LEVEL_7.getValue());
        });

        return true;
    }
}
