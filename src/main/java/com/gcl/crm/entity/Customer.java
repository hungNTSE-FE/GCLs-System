package com.gcl.crm.entity;

import com.gcl.crm.enums.Gender;
import com.gcl.crm.enums.Status;
import com.gcl.crm.form.CustomerStatusForm;
import com.gcl.crm.form.CustomerStatusEvaluationForm;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Entity
@Data
@Table(name = "CUSTOMER")
@SqlResultSetMapping(
        name = "getCustomerStatusListMapping",
        classes = @ConstructorResult(
                targetClass = CustomerStatusForm.class
                , columns = {
                @ColumnResult(name = "marketing_name", type = String.class),
                @ColumnResult(name = "level_0", type = String.class),
                @ColumnResult(name = "level_1", type = String.class),
                @ColumnResult(name = "level_2", type = String.class),
                @ColumnResult(name = "level_3", type = String.class),
                @ColumnResult(name = "level_4", type = String.class),
                @ColumnResult(name = "level_5", type = String.class),
                @ColumnResult(name = "level_6", type = String.class),
                @ColumnResult(name = "level_7", type = String.class),
                @ColumnResult(name = "total", type = String.class),
        }
        )
)
@SqlResultSetMapping(
        name = "getCustomerStatusReportListMapping",
        classes = @ConstructorResult(
                targetClass = CustomerStatusEvaluationForm.class
                , columns = {
                @ColumnResult(name = "marketing_name", type = String.class),
                @ColumnResult(name = "level_6", type = Integer.class),
                @ColumnResult(name = "level_7", type = Integer.class),
                @ColumnResult(name = "LOT", type = Integer.class),
            }
        )
)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMER_ID")
    private Integer customerId;

    @Column(name = "CUSTOMER_CODE", unique = true)
    private String customerCode;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "GENDER")
    private Gender gender;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "STATUS")
    private boolean status;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ACCOUNT_REGISTER_DATE")
    private Date accountRegisterDate;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name="trading_account")
    private String number;

    @Column(name="contract_number")
    private String contractNumber;

    @Column(name = "ADD_USER")
    private Long addUser;

    @Column(name = "UPD_DATE")
    private Date updDate;

    @Column(name = "UPD_USER")
    private Long updUser;

    @ManyToOne
    @JoinColumn(name = "LEVEL_ID")
    private Level level;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "identity_number", referencedColumnName = "identity_number")
    private Identification identification;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BankAccount> bankAccounts;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number", referencedColumnName = "account_number")
    private TradingAccount tradingAccount;

    @OneToOne
    @JoinColumn(name = "id")
    private Employee employee;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", referencedColumnName = "contract_id")
    private Contract contract;

    @OneToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private CustomerDistribution customerDistribution;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "SOURCE_ID")
    private Source source;

    private String birthDate;
    private String issueDate;

    @Column(name = "BANK_CODE")
    private String bankCode;

    @Column(name = "BANK_NAME")
    private String bankName;

    public Customer() {
    }

    public Customer(Integer customerId, String customerCode, String customerName, String phoneNumber, TradingAccount tradingAccount) {
        this.customerId = customerId;
        this.customerCode = customerCode;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.tradingAccount = tradingAccount;
    }

    public Customer(Integer customerId, String customerName, String phoneNumber, String email, String description) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.description = description;
    }
    public Customer(Integer customerId, String customerName, String phoneNumber, String email, String description, String number) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.description = description;
        this.number = number;
    }

    public Customer(Integer customerId, String customerName, String phoneNumber, String email, String number, String contractNumber, Source source) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.number = number;
        this.contractNumber = contractNumber;
        this.source = source;
    }
}
