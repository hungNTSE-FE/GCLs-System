package com.gcl.crm.entity;

import com.gcl.crm.enums.Status;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Potential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String phoneNumber;

    private String date;

    private String name;

    @Column(unique = true)
    private String email;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Source source;

    @Transient
    private String sourceName;

    private String address;

    private String mktId;

    private String mktTeam;

    private String sale;

    private String course;

    private String initialState;

    private String firstCare;

    private String secondCare;

    private String thirdCare;

    private String tradingAccount;

    private String marginAccount;

    private String note;

    // Id of last employee create potential
    private Long maker;

    // Date now to update potential
    private Date lastModified;

    // Id of last employee made changes to potential
    private Long lastModifier;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id")
    private Level level;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "potential")
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "potential")
    private List<Activity> activities;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "potential")
    private List<CustomerDistribution> customerDistributionList;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Potential)) {
            return false;
        }
        Potential potential = (Potential) o;
        if (this.phoneNumber == null){
            return this.email.equals(potential.getEmail());
        }
        if (this.email == null){
            return this.phoneNumber.equals(potential.getPhoneNumber());
        }
        return this.phoneNumber.equals(potential.getPhoneNumber())
                || this.email.equals(potential.getEmail());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMktId() {
        return mktId;
    }

    public void setMktId(String mktId) {
        this.mktId = mktId;
    }

    public String getMktTeam() {
        return mktTeam;
    }

    public void setMktTeam(String mktTeam) {
        this.mktTeam = mktTeam;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getInitialState() {
        return initialState;
    }

    public void setInitialState(String initialState) {
        this.initialState = initialState;
    }

    public String getFirstCare() {
        return firstCare;
    }

    public void setFirstCare(String firstCare) {
        this.firstCare = firstCare;
    }

    public String getSecondCare() {
        return secondCare;
    }

    public void setSecondCare(String secondCare) {
        this.secondCare = secondCare;
    }

    public String getThirdCare() {
        return thirdCare;
    }

    public void setThirdCare(String thirdCare) {
        this.thirdCare = thirdCare;
    }

    public String getTradingAccount() {
        return tradingAccount;
    }

    public void setTradingAccount(String tradingAccount) {
        this.tradingAccount = tradingAccount;
    }

    public String getMarginAccount() {
        return marginAccount;
    }

    public void setMarginAccount(String marginAccount) {
        this.marginAccount = marginAccount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getMaker() {
        return maker;
    }

    public void setMaker(Long maker) {
        this.maker = maker;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Long getLastModifier() {
        return lastModifier;
    }

    public void setLastModifier(Long lastModifier) {
        this.lastModifier = lastModifier;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<CustomerDistribution> getCustomerDistributionList() {
        return customerDistributionList;
    }

    public void setCustomerDistributionList(List<CustomerDistribution> customerDistributionList) {
        this.customerDistributionList = customerDistributionList;
    }
}
