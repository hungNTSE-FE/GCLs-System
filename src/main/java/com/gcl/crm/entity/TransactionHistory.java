package com.gcl.crm.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name="transaction_history")
public class TransactionHistory {
    @Id
    @Column(name="transaction_id")
    private Long transactionID;

    @Column(name="transaction_date")
    private Date transactionDate;

    @Column(name="transaction_type")
    private String transactionType ;

    @Column(name="lot")
    private int lot ;

    @Column(name="money")
    private String money;

    @Column(name="broker_code")
    private String broker_code;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_number")
    private TradingAccount tradingAccount;

    public TransactionHistory() {
    }

    public Long getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(Long transactionID) {
        this.transactionID = transactionID;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public int getLot() {
        return lot;
    }

    public void setLot(int lot) {
        this.lot = lot;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getBroker_code() {
        return broker_code;
    }

    public void setBroker_code(String broker_code) {
        this.broker_code = broker_code;
    }

    public TradingAccount getTradingAccount() {
        return tradingAccount;
    }

    public void setTradingAccount(TradingAccount tradingAccount) {
        this.tradingAccount = tradingAccount;
    }
}
