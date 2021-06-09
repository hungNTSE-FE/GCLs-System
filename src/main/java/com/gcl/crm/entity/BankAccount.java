package com.gcl.crm.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "bank_account")
public class BankAccount {

    @Id
    @Column(name = "account_number")
    private String id;

    private String bankName;

    private String ownerName;
}
