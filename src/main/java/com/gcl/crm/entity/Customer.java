package com.gcl.crm.entity;

import com.gcl.crm.enums.Gender;
import com.gcl.crm.form.CustomerStatusForm;
import com.gcl.crm.form.CustomerStatusReportForm;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@SqlResultSetMapping(
        name = "getCustomerStatusListMapping",
        classes = @ConstructorResult(
                targetClass = CustomerStatusForm.class
                , columns = {
                @ColumnResult(name = "employee_name", type = String.class),
                @ColumnResult(name = "level_0", type = String.class),
                @ColumnResult(name = "level_1", type = String.class),
                @ColumnResult(name = "level_2", type = String.class),
                @ColumnResult(name = "level_3", type = String.class),
                @ColumnResult(name = "level_4", type = String.class),
                @ColumnResult(name = "level_5", type = String.class),
                @ColumnResult(name = "level_6", type = String.class),
                @ColumnResult(name = "level_7", type = String.class)
        }
        )
)
@SqlResultSetMapping(
        name = "getCustomerStatusReportListMapping",
        classes = @ConstructorResult(
                targetClass = CustomerStatusReportForm.class
                , columns = {
                @ColumnResult(name = "employee_name", type = String.class),
                @ColumnResult(name = "level_6", type = Integer.class),
                @ColumnResult(name = "level_7", type = Integer.class)
            }
        )
)
@Table(name = "CUSTOMER")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CUSTOMER_CODE")
    private Long customerCode;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "AGE")
    private Integer age;

    @Column(name = "DATE_OF_BIRTH")
    private Date dateOfBirth;

    @Column(name = "GENDER")
    private Gender gender;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "REGISTER_DATE")
    private Date registerDate;

    @ManyToOne
    @JoinColumn(name = "LEVEL_ID")
    private Level level;

    @OneToOne
    @JoinColumn(name = "identity_number")
    private Identification identification;

    @ManyToMany
    @JoinColumn(name = "CAMPAIGN_CODE")
    private List<Campaign> campaignList;

    @OneToOne
    @JoinColumn(name = "id")
    private Employee employee;

    public Long getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(Long customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Identification getIdentification() {
        return identification;
    }

    public void setIdentification(Identification identification) {
        this.identification = identification;
    }

    public List<Campaign> getCampaignList() {
        return campaignList;
    }

    public void setCampaignList(List<Campaign> campaignList) {
        this.campaignList = campaignList;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
