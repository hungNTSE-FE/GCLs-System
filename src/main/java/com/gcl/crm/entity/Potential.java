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

    private String tradingAccount;

    private String marginAccount;

    private String note;

    // Id of employee create potential
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
}
