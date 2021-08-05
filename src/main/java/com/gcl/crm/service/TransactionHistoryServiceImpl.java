package com.gcl.crm.service;

import com.gcl.crm.entity.*;
import com.gcl.crm.enums.Status;
import com.gcl.crm.repository.TradingAccountRepository;
import com.gcl.crm.repository.TransactionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    @Override
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

        return true;
    }
}
