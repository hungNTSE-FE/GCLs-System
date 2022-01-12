package com.gcl.crm.service;

import com.gcl.crm.entity.TransactionHistory;
import com.gcl.crm.entity.User;

import java.util.List;

public interface TransactionHistoryService {

    List<TransactionHistory> getAllTransactionHistory();
    boolean importTransactionHistory(List<TransactionHistory> transactionHistories );
}
